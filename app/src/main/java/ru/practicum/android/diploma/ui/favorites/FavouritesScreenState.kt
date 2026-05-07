package ru.practicum.android.diploma.ui.favorites

import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

sealed interface FavouritesScreenState {
    object NothingFound : FavouritesScreenState
    object Error : FavouritesScreenState
    data class Content(val vacancy: VacancyDetails) : FavouritesScreenState
}
