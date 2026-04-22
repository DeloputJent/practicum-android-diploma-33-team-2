package ru.practicum.android.diploma.ui.favorites.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorites.FavoritesViewModel

val favoritesModule = module {
    viewModelOf(::FavoritesViewModel)
}
