package ru.practicum.android.diploma.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard
import ru.practicum.android.diploma.ui.vacancy.VacancySearchUiState

class DetailViewModel(private val vacancyId:String,
                      private val favoriteInteractor: FavoriteVacancyInteractor
    ): ViewModel() {
    var isTrackFavorite : Boolean=false
    private val playerStateLiveData = MutableLiveData<VacancySearchUiState>(
        VacancySearchUiState.Loading
    )

    fun observePlayerState() : LiveData<VacancySearchUiState> = playerStateLiveData

    fun checkIsVacancyFavorite() {
        viewModelScope.launch {
            isTrackFavoriteLiveData.value=favoriteInteractor.getVacanciesId().contains(vacancyId)
        }
    }

    private val isTrackFavoriteLiveData = MutableLiveData<Boolean>(
        isTrackFavorite
    )

    fun onFavoriteClicked() {
        isTrackFavorite = isTrackFavoriteLiveData.value?:false
        if (isTrackFavorite) {
            isTrackFavoriteLiveData.value=false
            viewModelScope.launch {
                favoriteInteractor.deleteVacancyById(vacancyId)
            }
        } else {
            isTrackFavoriteLiveData.value=true
            isTrackFavorite = isTrackFavoriteLiveData.value?:false
            viewModelScope.launch {
                favoriteInteractor.insertVacancy(convertToVacancyCard(currentVacancy))
            }
        }
    }

    private fun convertToVacancyCard(vacancy: VacancyDetails): VacancyCard {
        return VacancyCard(
            id = vacancy.id,
            name = vacancy.name,
            company = vacancy.employerName,
            city = vacancy.address,
            salary = vacancy.salary,
            logo = vacancy.employerLogo
        )
    }
}
