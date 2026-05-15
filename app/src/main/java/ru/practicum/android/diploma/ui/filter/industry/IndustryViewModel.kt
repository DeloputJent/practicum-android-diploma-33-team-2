package ru.practicum.android.diploma.ui.filter.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterInteractor
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class IndustryViewModel(
    private val interactor: SearchWithFilterInteractor,
    private val saveFilter: FilterSettingsInteractor,
) : ViewModel() {
    private val _state = MutableStateFlow<IndustryListScreenState>(IndustryListScreenState.Loading)
    val state: StateFlow<IndustryListScreenState> = _state.asStateFlow()
    var industryScroll: MutableList<FilterIndustry> = mutableListOf()
    var filteredScroll: MutableList<FilterIndustry> = mutableListOf()
    private var selectedIndustry = FilterIndustry()
    private var preSelectedIndustry = FilterIndustry()
    fun chooseSelectedIndustry(selectedIndustry: FilterIndustry) {
        this.selectedIndustry = selectedIndustry
    }
    fun loadPreSelectedIndustryId() {
        val filter = saveFilter.getFilterSettings()
        if (filter.industryId != null) {
            preSelectedIndustry = preSelectedIndustry.copy(
                id = filter.industryId.toString(),
                name = filter.industryName
            )
        }
    }
    fun saveSelectedIndustry() {
        val filterSet = saveFilter.getFilterSettings()
        saveFilter.updateFilterSettings(
            filterSet.copy(
                industryId = selectedIndustry.id?.toInt(),
                industryName = selectedIndustry.name,
            )
        )
    }
    fun observeFilteredScroll(query: String) {
        filteredScroll = filteredByText(query)
        _state.value = IndustryListScreenState.Content(filteredScroll.toList())
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
                        industryScroll = data.items.toMutableList()
                        data.items.forEach {
                            it.flagOfSelection = it == preSelectedIndustry
                        }
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
    private fun filteredByText(string: String): MutableList<FilterIndustry> {
        val filteredList: MutableList<FilterIndustry> = mutableListOf()
        industryScroll.forEach {
            if (it.name?.contains(string) ?: false) filteredList.add(it)
        }
        return filteredList
    }
}
