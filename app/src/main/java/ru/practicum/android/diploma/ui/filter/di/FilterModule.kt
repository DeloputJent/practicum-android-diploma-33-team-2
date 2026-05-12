package ru.practicum.android.diploma.ui.filter.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.filter.industry.IndustryViewModel

val filterModule = module {
    viewModel {
        FilterViewModel(get())
    }
    viewModel {
        IndustryViewModel(
            get(),
            saveFilter = get()
        )
    }
}
