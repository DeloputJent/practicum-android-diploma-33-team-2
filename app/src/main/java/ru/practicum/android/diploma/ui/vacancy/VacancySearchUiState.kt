package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.search.models.VacancyShort

sealed interface VacancySearchUiState {
    data object Initial : VacancySearchUiState
    data object Loading : VacancySearchUiState
    data class Content(val found: Int, val items: List<VacancyShort>) : VacancySearchUiState
    data object Empty : VacancySearchUiState
    data object NoInternet : VacancySearchUiState
    data object ServerError : VacancySearchUiState
}
