package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.filter.IndustryResponseDto
import ru.practicum.android.diploma.data.filter.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry
import ru.practicum.android.diploma.domain.filter.models.IndustryListResult

class IndustryListDtoConverter {

    fun map(responseDto: IndustryResponseDto): IndustryListResult {
        return IndustryListResult(
            items = responseDto.items.map { map(it) }
        )
    }

    private fun map(responseDto: FilterIndustryDto): FilterIndustry {
        return FilterIndustry(
            id = responseDto.id,
            name = responseDto.name
        )
    }
}
