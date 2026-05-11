package ru.practicum.android.diploma.ui.filter.industry

import android.util.Log
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
        Log.d("industry", "observeIndustryList")
        viewModelScope.launch {
            _state.value = IndustryListScreenState.Loading
            when (val result = interactor.getAllIndustryList()) {
                is Resource.Success -> {
                    val data = result.data
                    if (data == null) {
                        Log.d("industry", "failure")
                        _state.value = IndustryListScreenState.ServerError
                    } else {
                        Log.d("industry", "data[1]=${data.items[1].name}")
                        _state.value = IndustryListScreenState.Content(data.items)
                    }
                }
                is Resource.Loading -> {
                    Log.d("industry", "loading")
                    _state.value = IndustryListScreenState.Loading
                }
                is Resource.Error -> {
                    Log.d("industry", "error ${result.kind}")
                    _state.value = when (result.kind) {
                        ErrorKind.NO_INTERNET, ErrorKind.SERVER -> IndustryListScreenState.ServerError
                    }
                }
            }
        }
    }
}
