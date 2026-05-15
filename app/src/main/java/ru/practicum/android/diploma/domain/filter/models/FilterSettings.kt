package ru.practicum.android.diploma.domain.filter.models

data class FilterSettings(
    val searchField: String? = null,
    val industryId: Int? = null,
    val industryName: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
) {
    fun isSettingsEmpty(): Boolean {
        return industryId == null &&
            industryName == null &&
            salary == null &&
            !onlyWithSalary
    }
}
