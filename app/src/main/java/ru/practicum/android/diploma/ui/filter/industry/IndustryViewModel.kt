package ru.practicum.android.diploma.ui.filter.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterInteractor
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class IndustryViewModel(private val interactor: SearchWithFilterInteractor,) : ViewModel() {
    private val _state = MutableStateFlow<IndustryListScreenState>(IndustryListScreenState.Loading)
    val state: StateFlow<IndustryListScreenState> = _state.asStateFlow()

    init {
        observeIndustryList()
    }

    fun observeIndustryList() {
        viewModelScope.launch {
            _state.value = IndustryListScreenState.Loading
            when (val result = interactor.getAllIndustryList()) {
                is Resource.Success -> {
                    val data = result.data
                    if (data == null) {
                        _state.value = IndustryListScreenState.ServerError
                    } else {
                        _state.value = IndustryListScreenState.Content(data.items)
                    }
                }
                is Resource.Loading -> {
                    _state.value = IndustryListScreenState.Loading
                }
                is Resource.Error -> {
                    _state.value = when (result.kind) {
                        ErrorKind.NO_INTERNET, ErrorKind.SERVER -> IndustryListScreenState.ServerError
                    }
                }
            }
        }
    }
}
