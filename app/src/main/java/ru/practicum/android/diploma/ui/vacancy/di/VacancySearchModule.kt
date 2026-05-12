package ru.practicum.android.diploma.ui.vacancy.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.vacancy.VacancySearchViewModel

val vacancySearchModule = module {
    single {
        androidContext().resources.getInteger(R.integer.search_debounce_ms).toLong()
    }

    viewModel {
        VacancySearchViewModel(
            get(),
            get(),
            get()
        )
    }
}
