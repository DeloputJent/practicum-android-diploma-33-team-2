package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.IndustryListDtoConverter
import ru.practicum.android.diploma.data.converters.SearchDtoConverter
import ru.practicum.android.diploma.data.dto.search.VacanciesSearchResponseDto
import ru.practicum.android.diploma.data.filter.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterRepository
import ru.practicum.android.diploma.domain.filter.models.IndustryListResult
import ru.practicum.android.diploma.domain.search.models.SearchResult
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class SearchWithFilterRepositoryImpl(
    private val api: HhApi,
    private val converter: SearchDtoConverter,
    private val industryConverter: IndustryListDtoConverter,
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
                            Resource.Error<IndustryListResult>(ErrorKind.NO_INTERNET)
                        } else {
                            val dtoList: List<FilterIndustryDto> = gson.fromJson(
                                json,
                                object : TypeToken<List<FilterIndustryDto>>() {}.type
                            )
                            Resource.Success(
                                IndustryListResult(items = dtoList.map { industryConverter.map(it) })
                            )
                        }
                    }
                }
            } catch (_: JsonIOException) {
                Resource.Error<IndustryListResult>(ErrorKind.SERVER)
            } catch (_: IOException) {
                Resource.Error<IndustryListResult>(ErrorKind.NO_INTERNET)
            } catch (_: Exception) {
                Resource.Error<IndustryListResult>(ErrorKind.SERVER)
            }
        }
    }

    override suspend fun getFilteredVacancy(
        query: String,
        page: Int,
        industryId: Int?,
        salary: Int?,
        onlyWithSalary: Boolean,
    ): Resource<SearchResult> {
        val filterOptions = fillFilterOptions(query, page, industryId, salary, onlyWithSalary)
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
                            Resource.Error<SearchResult>(ErrorKind.NO_INTERNET)
                        } else {
                            val dto = gson.fromJson(json, VacanciesSearchResponseDto::class.java)
                            Resource.Success(converter.map(dto))
                        }
                    }
                }
            } catch (_: JsonIOException) {
                Resource.Error<SearchResult>(ErrorKind.SERVER)
            } catch (_: IOException) {
                Resource.Error<SearchResult>(ErrorKind.NO_INTERNET)
            } catch (_: Exception) {
                Resource.Error<SearchResult>(ErrorKind.SERVER)
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

    fun fillFilterOptions(
        query: String,
        page: Int,
        industryId: Int?,
        salary: Int?,
        onlyWithSalary: Boolean
    ): HashMap<String, String> {
        val filterOptions: HashMap<String, String> = HashMap()
        filterOptions["text"] = query
        filterOptions["page"] = page.toString()
        if (industryId != null) {
            filterOptions["industry"] = industryId.toString()
        }
        if (salary != null) {
            filterOptions["salary"] = salary.toString()
        }
        filterOptions["only_with_salary"] = if (onlyWithSalary) "true" else "false"
        return filterOptions
    }
    companion object {
        private const val SERVER_ERROR = 500
        private const val NO_RESOURCE_FOUND = 404
    }
}
