package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.data.dto.search.VacancyCardDto
import ru.practicum.android.diploma.data.dto.search.VacancyCardSalaryDto
import ru.practicum.android.diploma.domain.search.models.Salary
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.domain.search.models.VacancyShort

class SearchDtoConverter {
    fun map(responseDto: VacanciesSearchResponseDto): SearchResult {
        return SearchResult(
            found = responseDto.found,
            items = responseDto.items.map { map(it) }
        )
    }
    private fun map(vacancyDto: VacancyCardDto): VacancyShort {
        return VacancyShort(
            id = vacancyDto.id,
            name = vacancyDto.name,
            company = vacancyDto.company,
            city = vacancyDto.city,
            salary = vacancyDto.salary?.let { map(it) },
            logo = vacancyDto.logo
        )
    }
    private fun map(salaryDto: VacancyCardSalaryDto): Salary {
        return Salary(
            from = salaryDto.from,
            to = salaryDto.to,
            currency = salaryDto.currency
        )
    }
}
