package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.models.VacancyShort
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class VacancySearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val debounceMs: Long,
) : ViewModel() {

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
                    // HH API in this project behaves like 1-based paging: request page=1 to get first page.
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

        if (isFirstPage) {
            _state.value = VacancySearchUiState.Loading
        } else {
            _isNextPageLoading.value = true
        }

        viewModelScope.launch {
            when (val result = searchInteractor.searchVacancies(currentQuery, page)) {
                is Resource.Success -> {
                    _isNextPageLoading.value = false

                    val data = result.data
                    if (data == null) {
                        if (isFirstPage) {
                            _state.value = VacancySearchUiState.ServerError
                        } else {
                            _toast.emit("Произошла ошибка")
                        }
                        return@launch
                    }

                    loadedPages.add(page)
                    currentPage = data.page
                    maxPages = data.pages

                    for (item in data.items) {
                        if (vacancyIds.add(item.id)) {
                            vacancies.add(item)
                        }
                    }

                    if (vacancies.isEmpty()) {
                        _state.value = VacancySearchUiState.Empty
                    } else {
                        _state.value = VacancySearchUiState.Content(data.found, vacancies.toList())
                    }
                }
                is Resource.Error -> {
                    _isNextPageLoading.value = false

                    if (isFirstPage) {
                        _state.value = when (result.kind) {
                            ErrorKind.NO_INTERNET -> VacancySearchUiState.NoInternet
                            ErrorKind.SERVER -> VacancySearchUiState.ServerError
                        }
                    } else {
                        when (result.kind) {
                            ErrorKind.NO_INTERNET -> _toast.emit("Проверьте подключение к интернету")
                            ErrorKind.SERVER -> _toast.emit("Произошла ошибка")
                        }
                    }
                }
                Resource.Loading -> Unit
            }
        }
    }
}
