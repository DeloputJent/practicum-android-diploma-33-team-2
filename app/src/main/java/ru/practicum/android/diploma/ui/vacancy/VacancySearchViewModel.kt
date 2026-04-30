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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.util.Resource

class VacancySearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val _state = MutableStateFlow<VacancySearchUiState>(VacancySearchUiState.Initial)
    val state: StateFlow<VacancySearchUiState> = _state.asStateFlow()

    init {
        observeQuery()
    }

    fun onQueryChanged(query: String) {
        queryFlow.update { query }
        if (query.isBlank()) {
            _state.value = VacancySearchUiState.Initial
        }
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(2000)
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect { query ->
                    _state.value = VacancySearchUiState.Loading
                    when (val result = searchInteractor.searchVacancies(query)) {
                        is Resource.Success -> {
                            val data = result.data
                            if (data == null || data.items.isEmpty()) {
                                _state.value = VacancySearchUiState.Initial
                            } else {
                                _state.value = VacancySearchUiState.Content(data.found, data.items)
                            }
                        }
                        is Resource.Error, Resource.Loading -> {
                            _state.value = VacancySearchUiState.Initial
                        }
                    }
                }
        }
    }
}
