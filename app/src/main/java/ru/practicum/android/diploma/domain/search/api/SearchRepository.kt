package ru.practicum.android.diploma.domain.search.api

import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    suspend fun searchVacancies(query: String, page: Int): Resource<SearchResult>
}
