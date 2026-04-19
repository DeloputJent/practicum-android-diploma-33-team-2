package ru.practicum.android.diploma

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class HHApplication():Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}
