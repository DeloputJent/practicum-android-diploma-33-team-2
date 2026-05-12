package ru.practicum.android.diploma.data.filter

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.filter.models.FilterSettings

class FilterSettingsRepositoryImpl(
    private val settingsStorage: SharedPreferences,
    private val gson: Gson
) : FilterSettingsRepository {
    override fun getFilterSettings(): FilterSettings {
        val json = settingsStorage.getString(FILTER_SETTINGS, null)
        return if (json == null) {
            defaultSettings
        } else {
            Log.d("set", "json read=$json")
            gson.fromJson(json, FilterSettings::class.java)
        }
    }

    override fun updateFilterSettings(settings: FilterSettings) {
        val json = gson.toJson(settings)
        Log.d("set", "json write=$json")
        settingsStorage.edit {
            putString(FILTER_SETTINGS, json)
        }
    }

    override fun clearFilterSettings() {
        val json = gson.toJson(defaultSettings)
        settingsStorage.edit {
            putString(FILTER_SETTINGS, json)
        }
    }
    companion object {
        val defaultSettings = FilterSettings(null, null, null, null, false)
        const val FILTER_SETTINGS = "Filter_settings"
    }
}
