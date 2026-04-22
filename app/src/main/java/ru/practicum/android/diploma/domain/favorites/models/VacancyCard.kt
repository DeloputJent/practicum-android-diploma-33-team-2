package ru.practicum.android.diploma.domain.favorites.models

data class VacancyCard(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: String?,
    val logo: String?,
)
