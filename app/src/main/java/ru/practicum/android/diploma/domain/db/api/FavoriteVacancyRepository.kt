package ru.practicum.android.diploma.domain.db.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

interface FavoriteVacancyRepository {

    suspend fun insertVacancy(favoriteVacancy: VacancyCard)

    suspend fun getVacancies(): Flow<List<VacancyCard>>

    suspend fun getVacanciesId(): List<String>

    suspend fun getVacancyById(vacancyId: String): VacancyCard?

    suspend fun saveVacancyDetails(details: VacancyDetails)

    suspend fun getVacancyDetailsById(vacancyId: String): VacancyDetails?

    suspend fun deleteVacancyDetailsById(vacancyId: String)

    suspend fun deleteVacancyById(vacancyId: String)
}
