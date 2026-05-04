package ru.practicum.android.diploma.domain.detail.api

import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.util.Resource

interface VacancyDetailInteractor {
    suspend fun getVacancyDetail(vacancyId: String): Resource<VacancyDetailResult>
}
