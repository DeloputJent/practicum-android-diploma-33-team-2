package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.filter.models.FilterSettings

interface FilterSettingsInteractor {
    fun getFilterSettings(): FilterSettings
    fun updateFilterSettings(settings: FilterSettings)
    fun clearFilterSettings()
}
