package ru.practicum.android.diploma.domain.filter.impl

import ru.practicum.android.diploma.domain.filter.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.filter.models.FilterSettings

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository,
) : FilterSettingsInteractor {
    override fun getFilterSettings(): FilterSettings {
        return repository.getFilterSettings()
    }
    override fun updateFilterSettings(settings: FilterSettings) {
        repository.updateFilterSettings(settings)
    }
    override fun clearFilterSettings() {
        repository.clearFilterSettings()
    }
}
