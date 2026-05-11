package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import com.google.gson.JsonIOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.IndustryListDtoConverter
import ru.practicum.android.diploma.data.converters.SearchDtoConverter
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsDto
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.data.filter.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterRepository
import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class SearchWithFilterRepositoryImpl(
    private val api: HhApi,
    private val converter: SearchDtoConverter,
    private val idustryConverter: IndustryListDtoConverter,
    private val gson: Gson,
) : SearchWithFilterRepository {

    override suspend fun getAllIndustryList(): Resource<IndustryListResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getIndustries(
                    authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                )
                when {
                    !response.isSuccessful -> isIndustryResponseUnsuccessful(response.code())
                    else -> {
                        val json = response.body()?.string()
                        if (json.isNullOrEmpty()) {
                            Resource.Error(ErrorKind<IndustryListResult>.NO_INTERNET)
                        } else {
                            val dto = gson.fromJson(json, FilterIndustryDto::class.java)
                            Resource.Success(idustryConverter.map(dto))
                        }
                    }
                }
            } catch (_: JsonIOException) {
                Resource.Error<SearchResult>(ErrorKind<IndustryListResult>.SERVER)
            } catch (_: IOException) {
                Resource.Error<SearchResult>(ErrorKind<IndustryListResult>.NO_INTERNET)
            } catch (_: Exception) {
                Resource.Error<SearchResult>(ErrorKind<IndustryListResult>.SERVER)
            }
        }
    }

    override suspend fun getFilteredVacancy(
        filterOptions: HashMap<String, String>
    ): Resource<SearchResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchVacanciesWithFilters(
                    authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    filterOptions = filterOptions,
                )
                when {
                    !response.isSuccessful -> isResponseUnsuccessful(response.code())
                    else -> {
                        val json = response.body()?.string()
                        if (json.isNullOrEmpty()) {
                            Resource.Error(ErrorKind<SearchResult>.NO_INTERNET)
                        } else {
                            val dto = gson.fromJson(json, VacanciesSearchResponseDto::class.java)
                            Resource.Success(converter.map(dto))
                        }
                    }
                }
            } catch (_: JsonIOException) {
                Resource.Error<SearchResult>(ErrorKind<SearchResult>.SERVER)
            } catch (_: IOException) {
                Resource.Error<SearchResult>(ErrorKind<SearchResult>.NO_INTERNET)
            } catch (_: Exception) {
                Resource.Error<SearchResult>(ErrorKind<SearchResult>.SERVER)
            }
        }
    }
    private fun isResponseUnsuccessful(httpCode: Int): Resource<SearchResult> {
        return when (httpCode) {
            SERVER_ERROR -> Resource.Error(ErrorKind.SERVER)
            NO_RESOURCE_FOUND -> Resource.Error(ErrorKind.NO_INTERNET)
            else -> Resource.Error(ErrorKind.SERVER)
        }
    }

    private fun isIndustryResponseUnsuccessful(httpCode: Int): Resource<IndustryListResult> {
        return when (httpCode) {
            SERVER_ERROR -> Resource.Error(ErrorKind.SERVER)
            NO_RESOURCE_FOUND -> Resource.Error(ErrorKind.NO_INTERNET)
            else -> Resource.Error(ErrorKind.SERVER)
        }
    }
    companion object {
        private const val SERVER_ERROR = 500
        private const val NO_RESOURCE_FOUND = 404
    }
}
