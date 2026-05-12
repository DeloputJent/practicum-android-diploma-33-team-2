package ru.practicum.android.diploma.domain.filter.models

data class FilterIndustry(
    val id: String?=null,
    val name: String?=null,
) {
    var flagOfSelection : Boolean = false
}
