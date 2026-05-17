package ru.practicum.android.diploma.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface HhApi {
    @GET("vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Query("text") text: String,
        @Query("page") page: Int,
    ): Response<ResponseBody>

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String,
    ): Response<ResponseBody>

    @GET("/industries")
    suspend fun getIndustries(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
    ): Response<ResponseBody>

    @GET("vacancies")
    suspend fun searchVacanciesWithFilters(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @QueryMap filterOptions: Map<String, String>
    ): Response<ResponseBody>
}
