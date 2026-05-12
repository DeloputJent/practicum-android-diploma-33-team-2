package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterInteractor
import ru.practicum.android.diploma.domain.filter.models.FilterSettings
import ru.practicum.android.diploma.domain.search.models.VacancyShort
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class VacancySearchViewModel(
    private val debounceMs: Long,
    private val filterStorage: FilterSettingsInteractor,
    private val searchWithFilterInteractor: SearchWithFilterInteractor,
) : ViewModel() {
    val filterSettingsLiveData = MutableLiveData<FilterSettings>()
    fun observeFilterSettingsState(): LiveData<FilterSettings> = filterSettingsLiveData
    private val queryFlow = MutableStateFlow("")
    private val _state = MutableStateFlow<VacancySearchUiState>(VacancySearchUiState.Initial)
    val state: StateFlow<VacancySearchUiState> = _state.asStateFlow()
    private val _isNextPageLoading = MutableStateFlow(false)
    val isNextPageLoading: StateFlow<Boolean> = _isNextPageLoading.asStateFlow()
    private val _toast = MutableSharedFlow<String>()
    val toast: SharedFlow<String> = _toast.asSharedFlow()
    private var currentQuery = ""
    private var currentPage = 0
    private var maxPages = 0
    private val loadedPages = mutableSetOf<Int>()
    private val vacancyIds = mutableSetOf<String>()
    private val vacancies = mutableListOf<VacancyShort>()

    init {
        observeQuery()
        getStoragedFilterSettings()
    }

    fun onQueryChanged(query: String) {
        queryFlow.update { query }
        if (query.isBlank()) {
            resetPaging()
            _state.value = VacancySearchUiState.Initial
        }
    }

    fun onLastItemReached() {
        if (currentQuery.isBlank()) {
            return
        }
        if (_isNextPageLoading.value) {
            return
        }
        if (maxPages != 0 && currentPage >= maxPages) {
            return
        }

        val nextPage = currentPage + 1
        if (loadedPages.contains(nextPage)) {
            return
        }

        loadPage(page = nextPage, isFirstPage = false)
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(debounceMs)
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect { query ->
                    resetPaging()
                    currentQuery = query
                    loadPage(page = 1, isFirstPage = true)
                }
        }
    }

    private fun resetPaging() {
        currentQuery = ""
        currentPage = 0
        maxPages = 0
        loadedPages.clear()
        vacancyIds.clear()
        vacancies.clear()
        _isNextPageLoading.value = false
    }

    private fun loadPage(page: Int, isFirstPage: Boolean) {
        if (loadedPages.contains(page)) {
            return
        }
        if (_isNextPageLoading.value) {
            return
        }

        startLoading(isFirstPage)
        viewModelScope.launch {
            val result = searchWithFilterInteractor.getFilteredVacancy(
                currentQuery,
                page,
                filterSettingsLiveData.value?.industryId,
                filterSettingsLiveData.value?.salary,
                filterSettingsLiveData.value?.onlyWithSalary ?: false
            )
            stopLoading()
            handleResult(result, page, isFirstPage)
        }
    }

    private fun startLoading(isFirstPage: Boolean) {
        if (isFirstPage) {
            _state.value = VacancySearchUiState.Loading
        } else {
            _isNextPageLoading.value = true
        }
    }

    private fun stopLoading() {
        _isNextPageLoading.value = false
    }

    private suspend fun handleResult(result: Resource<ru.practicum.android.diploma.domain.search.models.SearchResult>, page: Int, isFirstPage: Boolean) {
        if (result is Resource.Success) {
            handleSuccess(result.data, page, isFirstPage)
        } else if (result is Resource.Error) {
            handleError(result.kind, isFirstPage)
        }
    }

    private suspend fun handleSuccess(
        data: ru.practicum.android.diploma.domain.search.models.SearchResult?,
        requestedPage: Int,
        isFirstPage: Boolean,
    ) {
        if (data == null) {
            if (isFirstPage) {
                _state.value = VacancySearchUiState.ServerError
            } else {
                _toast.emit("Произошла ошибка")
            }
            return
        }

        loadedPages.add(requestedPage)
        currentPage = data.page
        maxPages = data.pages

        addUniqueItems(data.items)

        if (vacancies.isEmpty()) {
            _state.value = VacancySearchUiState.Empty
        } else {
            _state.value = VacancySearchUiState.Content(data.found, vacancies.toList())
        }
    }

    private fun addUniqueItems(items: List<VacancyShort>) {
        for (item in items) {
            if (vacancyIds.add(item.id)) {
                vacancies.add(item)
            }
        }
    }

    private suspend fun handleError(kind: ErrorKind, isFirstPage: Boolean) {
        if (isFirstPage) {
            if (kind == ErrorKind.NO_INTERNET) {
                _state.value = VacancySearchUiState.NoInternet
            } else {
                _state.value = VacancySearchUiState.ServerError
            }
        } else {
            if (kind == ErrorKind.NO_INTERNET) {
                _toast.emit("Проверьте подключение к интернету")
            } else {
                _toast.emit("Произошла ошибка")
            }
        }
    }

    fun getStoragedFilterSettings() {
        filterSettingsLiveData.value = filterStorage.getFilterSettings()
    }
}
