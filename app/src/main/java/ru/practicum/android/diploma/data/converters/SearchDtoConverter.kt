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
            pages = responseDto.pages,
            page = responseDto.page,
            items = responseDto.items.map { map(it) }
        )
    }
    private fun map(vacancyDto: VacancyCardDto): VacancyShort {
        val logo = vacancyDto.employer?.logoUrls?.size90
            ?: vacancyDto.employer?.logoUrls?.original
            ?: vacancyDto.employer?.logoUrls?.size240
            ?: vacancyDto.logo
            ?: vacancyDto.logoPath
        val company = vacancyDto.employer?.name ?: vacancyDto.company
        val city = vacancyDto.area?.name ?: vacancyDto.address?.city ?: vacancyDto.city
        return VacancyShort(
            id = vacancyDto.id,
            name = vacancyDto.name,
            company = company,
            city = city,
            salary = vacancyDto.salary?.let { map(it) },
            logo = logo
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
