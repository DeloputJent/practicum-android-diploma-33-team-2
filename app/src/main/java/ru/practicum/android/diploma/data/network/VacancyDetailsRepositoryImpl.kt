package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.VacancyDetailDtoConverter
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsDto
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsResponse
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailRepository
import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsRepositoryImpl(
    private val api: HhApi,
    private val converter: VacancyDetailDtoConverter,
    private val gson: Gson,
) : VacancyDetailRepository {
    override suspend fun getVacancyDetail(vacancyId: String): Resource<VacancyDetailResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVacancyDetails(
                    authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    id = vacancyId
                )
                when {
                    !response.isSuccessful -> isResponseUnsuccessful(response.code())
                    else -> {
                        val json = response.body()?.string()
                        if (json.isNullOrEmpty()) {
                            Resource.Error(ErrorKind.NO_INTERNET)
                        } else {
                            val vacancy = gson.fromJson(json, VacancyDetailsDto::class.java)
                            val dto = VacancyDetailsResponse(vacancy)
                            Resource.Success(converter.map(dto))
                        }
                    }
                }
            } catch (e: Exception) {
                Resource.Error(ErrorKind.SERVER)
            }
        }
    }
    private fun isResponseUnsuccessful(httpCode: Int): Resource<VacancyDetailResult> {
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
