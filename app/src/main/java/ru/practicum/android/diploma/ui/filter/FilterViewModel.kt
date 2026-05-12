package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.filter.models.FilterSettings

class FilterViewModel(private val filterStorage: FilterSettingsInteractor,) : ViewModel() {
    init { getStoragedFilterSettings() }
    var filterSettings = FilterSettings()
    val filterSettingsLiveData = MutableLiveData<FilterSettings>(
        filterSettings
    )
    fun observeFilterSettingsState(): LiveData<FilterSettings> = filterSettingsLiveData

    fun saveFilterSettings() {
        filterStorage.updateFilterSettings(filterSettings)
    }
    fun clearFilterSettings() {
        filterStorage.clearFilterSettings()
    }
    private fun getStoragedFilterSettings() {
        filterSettings = filterStorage.getFilterSettings()
    }
}
