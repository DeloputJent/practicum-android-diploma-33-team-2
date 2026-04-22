package ru.practicum.android.diploma.ui.filter.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.filter.FilterViewModel

val filterModule = module {
    viewModelOf(::FilterViewModel)
}
