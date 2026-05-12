package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.filter.models.FilterSettings

interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettings
    fun updateFilterSettings(settings: FilterSettings)
}
