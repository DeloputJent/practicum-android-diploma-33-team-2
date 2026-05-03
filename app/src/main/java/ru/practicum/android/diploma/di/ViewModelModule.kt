package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.details.DetailViewModel

val ViewModelModule = module {
    viewModel {
        DetailViewModel(get(), get(), get(), get())
    }
}
