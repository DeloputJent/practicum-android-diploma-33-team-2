package ru.practicum.android.diploma.data.dto.search

data class VacanciesSearchResponseDto(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>,
)
