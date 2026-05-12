package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_details_table")
data class VacancyDetailsEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val salary: String,
    val address: String?,
    val experience: String?,
    val scheduleAndEmployment: String?,
    val contactsName: String?,
    val contactsEmail: String?,
    val phonesJson: String?,
    val employerName: String,
    val areaName: String,
    val skills: String?,
    val url: String,
    val industryName: String,
)

