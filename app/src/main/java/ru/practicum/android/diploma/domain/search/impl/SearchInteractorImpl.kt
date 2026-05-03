package ru.practicum.android.diploma.domain.search.impl

import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

class SearchInteractorImpl(
    private val repository: SearchRepository,
) : SearchInteractor {

    override suspend fun searchVacancies(query: String): Resource<SearchResult> {
        return repository.searchVacancies(query)
    }
}
