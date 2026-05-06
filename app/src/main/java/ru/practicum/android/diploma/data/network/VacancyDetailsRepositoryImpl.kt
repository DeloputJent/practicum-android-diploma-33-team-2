package ru.practicum.android.diploma.data.network

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.VacancyDetailDtoConverter
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsResponse
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailRepository
import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsRepositoryImpl(
    private val api: HhApi,
    private val converter: VacancyDetailDtoConverter,
    private val gson: Gson,
) : VacancyDetailRepository {

    override suspend fun getVacancyDetail(vacancyId: String): Resource<VacancyDetailResult> = withContext(
        Dispatchers.IO
    ) {
        try {
            val response = api.searchVacancies(
                authorization = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                text = vacancyId,
                page = 0,
            )

            if (!response.isSuccessful) {
                return@withContext Resource.Error()
            }

            val json = response.body()?.string()
            if (json.isNullOrEmpty()) {
                return@withContext Resource.Error()
            }

            val dto = gson.fromJson(json, VacancyDetailsResponse::class.java)
            Resource.Success(converter.map(dto))
        } catch (e: Exception) {
            Resource.Error()
        }
    }
}
