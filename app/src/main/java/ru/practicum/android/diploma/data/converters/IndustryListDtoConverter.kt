package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.data.dto.search.VacancyCardDto
import ru.practicum.android.diploma.data.dto.search.VacancyCardSalaryDto
import ru.practicum.android.diploma.data.filter.IndustryResponseDto
import ru.practicum.android.diploma.data.filter.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry
import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.models.Salary
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.domain.search.models.VacancyShort

class IndustryListDtoConverter {

    fun map(responseDto: IndustryResponseDto): IndustryListResult {
        return IndustryListResult(
            items =responseDto.items.map { map(it) }
        )
    }

    private fun map(responseDto: FilterIndustryDto): FilterIndustry {
        return FilterIndustry(
            id = responseDto.id,
            name = responseDto.name
        )
    }
}
