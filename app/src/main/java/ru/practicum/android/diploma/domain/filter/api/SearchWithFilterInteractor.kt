package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

interface SearchWithFilterInteractor {
    suspend fun getAllIndustryList(): Resource<SearchResult>

    suspend fun getFilteredVacancy(
        query: String,
        page: Int,
        industryId : Int?,
        salary : Int?,
        onlyWithSalary:Boolean?,
    ): Resource<SearchResult>
}
