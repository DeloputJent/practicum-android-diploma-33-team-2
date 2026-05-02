package ru.practicum.android.diploma.domain.detail.model

data class VacancyDetails(
    val id: String,
    val name: String,
    val description: String,
    val salary: String?,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contactsName: String?,
    val contactsEmail: String?,
    val phones: List<Phone>?,
    val employerName: String,
    val employerLogo: String,
    val areaName:String,
    val skills: String?,
    val url: String,
    val industryName: String,
) {
    data class Phone(
        val comment: String?,
        val formatted: String
    )
}
