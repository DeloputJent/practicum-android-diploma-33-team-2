package ru.practicum.android.diploma.domain.filter.models

data class FilterSettings(
    val industryId: Int?,
    val salary: Int?,
    val onlyWithSalary: Boolean,
    ) { }
