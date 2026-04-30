package ru.practicum.android.diploma.data.dto.search

import com.google.gson.annotations.SerializedName

data class VacancyCardDto(
    val id: String,
    val name: String,
    val employer: EmployerDto?,
    val area: AreaDto?,
    val address: AddressDto?,
    val company: String?,
    val city: String?,
    val logo: String?,
    @SerializedName("logo_path")
    val logoPath: String?,
    val salary: VacancyCardSalaryDto?,
)

data class EmployerDto(
    val name: String?,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrlsDto?,
)

data class LogoUrlsDto(
    @SerializedName("90")
    val size90: String?,
    @SerializedName("240")
    val size240: String?,
    val original: String?,
)

data class AreaDto(
    val name: String?,
)

data class AddressDto(
    val city: String?,
)
