package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.db.impl.FavoriteVacancyInteractorImpl

val interactorModule = module {

    single<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(get())
    }
}
