package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.details.DetailViewModel

val ViewModelModule = module {
    viewModel { (vacancyId: String) ->
        DetailViewModel(
            vacancyId = vacancyId,
            favoriteInteractor = get(),
            detailsInteractor = get(),
            sharingInteractor = get(),
        )
    }
}
