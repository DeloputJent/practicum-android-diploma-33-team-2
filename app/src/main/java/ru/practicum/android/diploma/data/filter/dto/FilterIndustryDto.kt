package ru.practicum.android.diploma.data.filter.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FilterIndustryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 20261205L
    }
}
