package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.db.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.domain.detail.api.SharingInteractor
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailInteractor
import ru.practicum.android.diploma.domain.detail.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.detail.impl.VacancyDetailInteractorImpl
import ru.practicum.android.diploma.domain.filter.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.filter.api.SearchWithFilterInteractor
import ru.practicum.android.diploma.domain.filter.impl.FilterSettingsInteractorImpl
import ru.practicum.android.diploma.domain.filter.impl.SearchWithFilterInteractorImpl
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchInteractorImpl

val interactorModule = module {
    single<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(get())
    }
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<VacancyDetailInteractor> {
        VacancyDetailInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<SearchWithFilterInteractor> {
        SearchWithFilterInteractorImpl(get())
    }
    single<FilterSettingsInteractor> {
        FilterSettingsInteractorImpl(get())
    }
}
