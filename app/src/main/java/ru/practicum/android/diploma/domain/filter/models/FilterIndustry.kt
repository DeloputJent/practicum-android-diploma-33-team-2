package ru.practicum.android.diploma.domain.filter.models

data class FilterIndustry(
    val id: String,
    val name: String,
) {
    var flagOfSelection : Boolean = false
}
