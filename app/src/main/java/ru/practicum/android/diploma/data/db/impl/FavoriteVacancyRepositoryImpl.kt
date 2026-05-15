package ru.practicum.android.diploma.data.db.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.data.converters.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.VacancyDatabase
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class FavoriteVacancyRepositoryImpl(
    private val vacancyBase: VacancyDatabase,
    private val converter: VacancyDbConvertor,
    private val detailsConverter: VacancyDetailsDbConverter,
) : FavoriteVacancyRepository {
    override suspend fun insertVacancy(favoriteVacancy: VacancyCard) {
        vacancyBase.getVacancyDao().insertVacancy(
            convertFromVacancyCard(favoriteVacancy)
        )
    }

    override suspend fun getVacancies(): Flow<List<VacancyCard>> {
        return vacancyBase.getVacancyDao().getVacancies()
            .map { list ->
                list.map { entity -> converter.map(entity) }.reversed()
            }
    }

    override suspend fun getVacanciesId(): List<String> {
        return vacancyBase.getVacancyDao().getVacanciesId()
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyCard? {
        val entity = vacancyBase.getVacancyDao().getVacancyById(vacancyId) ?: return null
        return converter.map(entity)
    }

    override suspend fun saveVacancyDetails(details: VacancyDetails) {
        vacancyBase.getVacancyDetailsDao().insert(detailsConverter.map(details))
    }

    override suspend fun getVacancyDetailsById(vacancyId: String): VacancyDetails? {
        val entity = vacancyBase.getVacancyDetailsDao().getById(vacancyId) ?: return null
        return detailsConverter.map(entity)
    }

    override suspend fun deleteVacancyDetailsById(vacancyId: String) {
        vacancyBase.getVacancyDetailsDao().deleteById(vacancyId)
    }

    override suspend fun deleteVacancyById(vacancyId: String) {
        vacancyBase.getVacancyDao().deleteVacancyById(vacancyId)
    }

    private fun convertFromVacancyCard(vacancyCard: VacancyCard): VacancyCardEntity {
        return converter.map(vacancyCard)
    }

}
