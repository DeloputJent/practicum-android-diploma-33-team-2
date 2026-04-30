package ru.practicum.android.diploma.ui.vacancy.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.vacancy.VacancySearchViewModel

val vacancySearchModule = module {
    single(named("searchDebounceMs")) {
        androidContext().resources.getInteger(R.integer.search_debounce_ms).toLong()
    }
    org.koin.androidx.viewmodel.dsl.viewModel {
        VacancySearchViewModel(
            searchInteractor = get(),
            debounceMs = get(named("searchDebounceMs")),
        )
    }
}
