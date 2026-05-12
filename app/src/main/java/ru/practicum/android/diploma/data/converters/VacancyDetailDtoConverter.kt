package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.details.VacancyDetailsDto
import ru.practicum.android.diploma.data.dto.details.VacancyDetailsResponse
import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

class VacancyDetailDtoConverter {

    fun map(responseDto: VacancyDetailsResponse): VacancyDetailResult {
        return VacancyDetailResult(
            item = map(responseDto.item)
        )
    }

    private fun map(vacancyDetailsDto: VacancyDetailsDto): VacancyDetails {
        return VacancyDetails(
            id = vacancyDetailsDto.id,
            name = vacancyDetailsDto.name,
            description = vacancyDetailsDto.description,
            salary = convertSalary(vacancyDetailsDto.salary),
            address = convertAddress(vacancyDetailsDto.address, vacancyDetailsDto.area),
            experience = vacancyDetailsDto.experience?.name,
            scheduleAndEmployment = convertToScheduleAndEmployment(
                vacancyDetailsDto.schedule,
                vacancyDetailsDto.employment
            ),
            contactsName = vacancyDetailsDto.contacts?.name,
            contactsEmail = vacancyDetailsDto.contacts?.email,
            phones = convertPhones(vacancyDetailsDto.contacts?.phones),
            employerName = vacancyDetailsDto.name,
            employerLogo = vacancyDetailsDto.employer.logo,
            areaName = vacancyDetailsDto.area.name,
            skills = convertSkills(vacancyDetailsDto.skills),
            url = vacancyDetailsDto.url,
            industryName = vacancyDetailsDto.industry.name
        )
    }
    private fun convertSalary(salary: VacancyDetailsDto.Salary?): String {
        if (salary != null) {
            var salaryString = ""
            if (salary.from != null) salaryString = "от " + salary.from.toString() + " "
            if (salary.to != null) salaryString = salaryString + "до " + salary.to.toString() + " "
            if (salaryString.isNotEmpty()) {
                if (salary.currency != null) salaryString += convertCurrency(salary.currency)
                return salaryString
            }
        }
        return "Зарплата не указана"
    }

    private fun convertAddress(
        address: VacancyDetailsDto.Address?,
        areaName: VacancyDetailsDto.Area
    ): String {
        return address?.raw ?: areaName.name
    }

    private fun convertToScheduleAndEmployment(
        schedule: VacancyDetailsDto.Schedule?,
        employment: VacancyDetailsDto.Employment?
    ): String? {
        if (employment == null && schedule == null) return null

        return buildString {
            employment?.name?.let { append(it) }
            schedule?.name?.let {
                if (isNotEmpty()) append(", ")
                append(it)
            }
        }
    }

    private fun convertPhones(phones: List<VacancyDetailsDto.Phone>?): List<VacancyDetails.Phone>? {
        return phones?.map { VacancyDetails.Phone(it.comment, it.formatted) }
    }
    private fun convertSkills(skills: List<String>): String {
        var resultString = String()
        skills.forEach { resultString = "$resultString • $it\n" }
        return resultString.trim()
    }
    private fun convertCurrency(currency: String): String {
        return when (currency) {
            "RUR", "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "so'm"
            "GEL" -> "₾"
            "KGT" -> "сом"
            else -> ""
        }
    }
}
