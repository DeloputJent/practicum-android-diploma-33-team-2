package ru.practicum.android.diploma.domain.search.models

data class SearchResult(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyShort>,
)
