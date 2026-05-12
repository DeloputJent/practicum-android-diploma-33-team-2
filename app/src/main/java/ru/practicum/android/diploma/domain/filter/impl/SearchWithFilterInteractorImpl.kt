package ru.practicum.android.diploma.domain.filter.impl

import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterInteractor
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterRepository
import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

class SearchWithFilterInteractorImpl(
    private val repository: SearchWithFilterRepository,
) : SearchWithFilterInteractor {
    override suspend fun getAllIndustryList(): Resource<IndustryListResult> {
        return repository.getAllIndustryList()
    }

    override suspend fun getFilteredVacancy(
        query: String,
        page: Int,
        industryId: Int?,
        salary: Int?,
        onlyWithSalary: Boolean
    ): Resource<SearchResult> {
        return repository.getFilteredVacancy(
            query = query,
            page = page,
            industryId = industryId,
            salary = salary,
            onlyWithSalary = onlyWithSalary
        )
    }
}
