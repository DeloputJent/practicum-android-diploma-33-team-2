package ru.practicum.android.diploma.domain.search.models

data class SearchResult(
    val found: Int,
    val items: List<VacancyShort>,
)
