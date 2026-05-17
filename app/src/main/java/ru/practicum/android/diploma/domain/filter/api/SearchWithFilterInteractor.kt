package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

interface SearchWithFilterInteractor {
    suspend fun getAllIndustryList(): Resource<IndustryListResult>

    suspend fun getFilteredVacancy(
        query: String,
        page: Int,
        industryId: Int? = null,
        salary: Int? = null,
        onlyWithSalary: Boolean = false,
    ): Resource<SearchResult>
}
