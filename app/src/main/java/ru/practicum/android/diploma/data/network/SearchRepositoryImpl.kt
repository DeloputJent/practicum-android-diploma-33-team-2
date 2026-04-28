package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.SearchDtoConverter
import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(
    private val api: HhApi,
    private val converter: SearchDtoConverter,
    private val gson: Gson,
) : SearchRepository {

    override suspend fun searchVacancies(query: String): Resource<SearchResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchVacancies(
                    authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    text = query
                )

                if (!response.isSuccessful) {
                    return@withContext Resource.Error<SearchResult>()
                }

                val json = response.body()?.string()
                if (json.isNullOrEmpty()) {
                    return@withContext Resource.Error<SearchResult>()
                }

                val dto = gson.fromJson(json, VacanciesSearchResponseDto::class.java)
                Resource.Success(converter.map(dto))
            } catch (_: Exception) {
                Resource.Error<SearchResult>()
            }
        }
    }
}
