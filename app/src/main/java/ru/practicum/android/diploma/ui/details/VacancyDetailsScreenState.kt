package ru.practicum.android.diploma.ui.details

import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

sealed interface VacancyDetailsScreenState {
    object Loading : VacancyDetailsScreenState
    object NothingFound : VacancyDetailsScreenState
    object ServerError : VacancyDetailsScreenState
    data class Content(val vacancy: VacancyDetails) : VacancyDetailsScreenState
}
