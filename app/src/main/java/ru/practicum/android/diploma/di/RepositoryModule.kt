package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.data.details.ExternalNavigator
import ru.practicum.android.diploma.data.network.SearchRepositoryImpl
import ru.practicum.android.diploma.data.network.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.detail.api.IntentProvider
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailRepository
import ru.practicum.android.diploma.domain.search.api.SearchRepository

val repositoryModule = module {

    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(get(), get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get())
    }
    single<VacancyDetailRepository> {
        VacancyDetailsRepositoryImpl(get(), get(), get())
    }
    single<IntentProvider> {
        ExternalNavigator(androidContext())
    }
}
