package ru.practicum.android.diploma

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.di.ViewModelModule
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.ui.favorites.di.favoritesModule
import ru.practicum.android.diploma.ui.filter.di.filterModule
import ru.practicum.android.diploma.ui.vacancy.di.vacancySearchModule

class HHApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidContext(this@HHApplication)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                vacancySearchModule,
                favoritesModule,
                filterModule,
                ViewModelModule
            )
        }
    }
}
