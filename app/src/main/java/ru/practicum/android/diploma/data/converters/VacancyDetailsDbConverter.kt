package ru.practicum.android.diploma.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.entity.VacancyDetailsEntity
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

class VacancyDetailsDbConverter(
    private val gson: Gson,
) {
    fun map(details: VacancyDetails): VacancyDetailsEntity {
        return VacancyDetailsEntity(
            id = details.id,
            name = details.name,
            description = details.description,
            salary = details.salary,
            address = details.address,
            experience = details.experience,
            scheduleAndEmployment = details.scheduleAndEmployment,
            contactsName = details.contactsName,
            contactsEmail = details.contactsEmail,
            phonesJson = phonesToJson(details.phones),
            employerName = details.employerName,
            areaName = details.areaName,
            skills = details.skills,
            url = details.url,
            industryName = details.industryName,
        )
    }

    fun map(entity: VacancyDetailsEntity): VacancyDetails {
        return VacancyDetails(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            salary = entity.salary,
            address = entity.address,
            experience = entity.experience,
            scheduleAndEmployment = entity.scheduleAndEmployment,
            contactsName = entity.contactsName,
            contactsEmail = entity.contactsEmail,
            phones = jsonToPhones(entity.phonesJson),
            employerName = entity.employerName,
            employerLogo = "",
            areaName = entity.areaName,
            skills = entity.skills,
            url = entity.url,
            industryName = entity.industryName,
        )
    }

    private fun phonesToJson(phones: List<VacancyDetails.Phone>?): String? {
        if (phones == null) return null
        return gson.toJson(phones)
    }

    private fun jsonToPhones(json: String?): List<VacancyDetails.Phone>? {
        if (json.isNullOrBlank()) return null
        val type = object : TypeToken<List<VacancyDetails.Phone>>() {}.type
        return gson.fromJson(json, type)
    }
}
