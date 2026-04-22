package ru.practicum.android.diploma.data.db.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.data.db.VacancyDatabase
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class FavoriteVacancyRepositoryImpl(
    private val vacancyBase: VacancyDatabase,
    private val converter: VacancyDbConvertor,
) : FavoriteVacancyRepository {
    override suspend fun insertVacancy(favoriteVacancy: VacancyCard) {
        vacancyBase.getVacancyDao().insertVacancy(
            convertFromVacancyCard(favoriteVacancy)
        )
    }

    override suspend fun getVacancies(): Flow<List<VacancyCard>> = flow {
        val vacancyLists = vacancyBase.getVacancyDao().getVacancies()
        emit(convertFromVacancyCardEntityList(vacancyLists).reversed())
    }

    override suspend fun getVacanciesId(): List<String> {
        return vacancyBase.getVacancyDao().getVacanciesId()
    }

    override suspend fun deleteVacancy(favoriteVacancy: VacancyCard) {
        vacancyBase.getVacancyDao().deleteVacancy(convertFromVacancyCard(favoriteVacancy))
    }

    private fun convertFromVacancyCardEntityList(vacancyCardLists: List<VacancyCardEntity>): List<VacancyCard> {
        return vacancyCardLists.map { vacancyCardEntity -> converter.map(vacancyCardEntity) }
    }

    private fun convertFromVacancyCard(vacancyCard: VacancyCard): VacancyCardEntity {
        return converter.map(vacancyCard)
    }

}
