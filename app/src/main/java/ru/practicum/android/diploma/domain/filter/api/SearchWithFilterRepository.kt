package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

interface SearchWithFilterRepository  {
    suspend fun getAllIndustryList(): Resource<IndustryListResult>
    suspend fun getFilteredVacancy(
       filterOptions: HashMap<String, String>
    ): Resource<SearchResult>
}
