package ru.practicum.android.diploma.domain.filter.models

data class FilterSettings(
    val industryId: Int?=null,
    val industryName: String?=null,
    val salary: Int?=null,
    val onlyWithSalary: Boolean=false,
    ) { }
