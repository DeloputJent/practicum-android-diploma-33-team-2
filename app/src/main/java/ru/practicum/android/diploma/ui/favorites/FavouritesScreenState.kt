package ru.practicum.android.diploma.ui.favorites

import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

sealed interface FavouritesScreenState {
    object NothingFound : FavouritesScreenState
    object Loading : FavouritesScreenState
    data class Content(val listOfFavourites: List<VacancyCard>) : FavouritesScreenState
}
