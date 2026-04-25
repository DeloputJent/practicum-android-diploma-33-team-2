package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class VacancyDbConvertor {
    fun map(vacancy: VacancyCard): VacancyCardEntity {
        return VacancyCardEntity(
            vacancy.id,
            vacancy.name,
            vacancy.company,
            vacancy.city,
            vacancy.salary,
            vacancy.logo,
        )
    }

    fun map(vacancyEntity: VacancyCardEntity): VacancyCard {
        return VacancyCard(
            vacancyEntity.id,
            vacancyEntity.name,
            vacancyEntity.company,
            vacancyEntity.city,
            vacancyEntity.salary,
            vacancyEntity.logo,
        )
    }
}
