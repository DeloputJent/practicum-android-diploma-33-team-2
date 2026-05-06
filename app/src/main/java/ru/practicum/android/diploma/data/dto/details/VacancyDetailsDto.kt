package ru.practicum.android.diploma.data.dto.details

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VacancyDetailsDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("address") val address: Address?,
    @SerializedName("experience") val experience: Experience?,
    @SerializedName("schedule") val schedule: Schedule?,
    @SerializedName("employment") val employment: Employment?,
    @SerializedName("contacts") val contacts: Contacts?,
    @SerializedName("description") val description: String,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("area") val area: Area,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("industry") val industry: Industry,
) : Serializable {
    data class Salary(
        @SerializedName("id") val id: String,
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?,
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Address(
        @SerializedName("id") val id: String,
        @SerializedName("city") val city: String,
        @SerializedName("street") val street: String,
        @SerializedName("building") val building: String,
        @SerializedName("raw") val raw: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Experience(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Schedule(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Employment(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Contacts(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("phones") val phones: List<Phone>
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Phone(
        @SerializedName("comment") val comment: String?,
        @SerializedName("formatted") val formatted: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Employer(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("logo") val logo: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Area(
        @SerializedName("id") val id: Int,
        @SerializedName("parentId") val parentId: Int,
        @SerializedName("name") val name: String,
        @SerializedName("areas") val areas: List<Area>
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    data class Industry(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    ) : Serializable{
        companion object {
            const val serialVersionUID: Long = 20240506L
        }
    }
    companion object {
        const val serialVersionUID: Long = 20240506L
    }
}
