package ru.practicum.android.diploma.data.dto.search

import com.google.gson.annotations.SerializedName

data class VacanciesSearchResponseDto(
    val found: Int,
    val items: List<VacancyCardDto>,
)
