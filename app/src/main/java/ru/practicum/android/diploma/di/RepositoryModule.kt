package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyRepository

val repositoryModule = module {

    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(get(), get())
    }

}
