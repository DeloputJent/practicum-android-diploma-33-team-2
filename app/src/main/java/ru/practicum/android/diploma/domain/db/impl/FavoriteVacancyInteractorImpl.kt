package ru.practicum.android.diploma.domain.db.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class FavoriteVacancyInteractorImpl(
    private val favoriteVacancyRepository: FavoriteVacancyRepository
) : FavoriteVacancyInteractor {
    override suspend fun insertVacancy(favoriteVacancy: VacancyCard) {
        favoriteVacancyRepository.insertVacancy(favoriteVacancy)
    }

    override suspend fun getVacancies(): Flow<List<VacancyCard>> {
        return favoriteVacancyRepository.getVacancies()
    }

    override suspend fun getVacanciesId(): List<String> {
        return favoriteVacancyRepository.getVacanciesId()
    }

    override suspend fun deleteVacancy(favoriteVacancy: VacancyCard) {
        favoriteVacancyRepository.deleteVacancy(favoriteVacancy)
    }

}
