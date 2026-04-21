package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_card_table")
data class VacancyCardEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: String?,
    val logo: String?,
)
