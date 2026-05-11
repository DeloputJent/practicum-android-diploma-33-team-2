package ru.practicum.android.diploma.ui.filter.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.filter.industry.IndustryViewModel

val filterModule = module {
    viewModelOf(::FilterViewModel)
    viewModel { IndustryViewModel(get()) }
}
