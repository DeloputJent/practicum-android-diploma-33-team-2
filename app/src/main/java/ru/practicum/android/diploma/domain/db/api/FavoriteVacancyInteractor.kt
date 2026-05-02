package ru.practicum.android.diploma.domain.db.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

interface FavoriteVacancyInteractor {
    suspend fun insertVacancy(favoriteVacancy: VacancyCard)

    suspend fun getVacancies(): Flow<List<VacancyCard>>

    suspend fun getVacanciesId(): List<String>

    suspend fun deleteVacancyById(vacancyId : String)
}
