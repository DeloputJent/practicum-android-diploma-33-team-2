package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import com.google.gson.JsonIOException
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.SearchDtoConverter
import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(
    private val api: HhApi,
    private val converter: SearchDtoConverter,
    private val gson: Gson,
) : SearchRepository {

    override suspend fun searchVacancies(query: String, page: Int): Resource<SearchResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchVacancies(
                    authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    text = query,
                    page = page,
                )

                if (!response.isSuccessful) {
                    return@withContext Resource.Error<SearchResult>(ErrorKind.SERVER)
                }

                val json = response.body()?.string()
                if (json.isNullOrEmpty()) {
                    return@withContext Resource.Error<SearchResult>(ErrorKind.SERVER)
                }

                val dto = gson.fromJson(json, VacanciesSearchResponseDto::class.java)
                Resource.Success(converter.map(dto))
            } catch (_: JsonIOException) {
                Resource.Error<SearchResult>(ErrorKind.SERVER)
            } catch (_: IOException) {
                Resource.Error<SearchResult>(ErrorKind.NO_INTERNET)
            } catch (_: Exception) {
                Resource.Error<SearchResult>(ErrorKind.SERVER)
            }
        }
    }
}
