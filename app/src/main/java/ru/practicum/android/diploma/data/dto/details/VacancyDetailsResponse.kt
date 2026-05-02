package ru.practicum.android.diploma.data.dto.details

import ru.practicum.android.diploma.data.dto.Response

class VacancyDetailsResponse (
    val searchType: String,
    val expression: String,
    val result : VacancyDetailsDto
) : Response()
