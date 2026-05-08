package ru.practicum.android.diploma.ui.favorites.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorites.FavoritesViewModel

val favoritesModule = module {
    viewModel {
        FavoritesViewModel(
            interactor = get(),
        )
    }
}
