package ru.practicum.android.diploma.ui.vacancy.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.vacancy.VacancySearchViewModel

val vacancySearchModule = module {
    viewModelOf(::VacancySearchViewModel)
}
