package ru.practicum.android.diploma.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HhApi {

    @GET("vacancies")
    suspend fun searchVacancies(
        @Query("text") text: String
    ): Response<ResponseBody>
}
