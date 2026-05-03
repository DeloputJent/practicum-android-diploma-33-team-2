package ru.practicum.android.diploma.domain.search.models

data class VacancyShort(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: Salary?,
    val logo: String?,
)
