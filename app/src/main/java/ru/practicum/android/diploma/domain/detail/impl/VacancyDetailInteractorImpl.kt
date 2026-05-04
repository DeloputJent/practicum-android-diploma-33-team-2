package ru.practicum.android.diploma.domain.detail.impl

import ru.practicum.android.diploma.domain.detail.api.VacancyDetailInteractor
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailRepository
import ru.practicum.android.diploma.domain.detail.model.VacancyDetailResult
import ru.practicum.android.diploma.util.Resource

class VacancyDetailInteractorImpl(
    private val repository: VacancyDetailRepository
) : VacancyDetailInteractor {
    override suspend fun getVacancyDetail(vacancyId: String): Resource<VacancyDetailResult> {
        return repository.getVacancyDetail(vacancyId)
    }
}
