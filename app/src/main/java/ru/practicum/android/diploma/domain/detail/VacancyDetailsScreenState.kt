package ru.practicum.android.diploma.domain.detail

import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

sealed interface VacancyDetailsScreenState {
    data class Content (val vacancy: VacancyDetails) : VacancyDetailsScreenState
    object Loading : VacancyDetailsScreenState
    object NothingFound : VacancyDetailsScreenState
    object ServerError : VacancyDetailsScreenState
}
