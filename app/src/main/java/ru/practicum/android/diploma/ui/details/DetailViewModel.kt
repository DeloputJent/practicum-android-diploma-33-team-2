package ru.practicum.android.diploma.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.detail.api.SharingInteractor
import ru.practicum.android.diploma.domain.detail.api.VacancyDetailInteractor
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard
import ru.practicum.android.diploma.util.ErrorKind
import ru.practicum.android.diploma.util.Resource

class DetailViewModel(
    private val vacancyId: String,
    private val favoriteInteractor: FavoriteVacancyInteractor,
    private val detailsInteractor: VacancyDetailInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private var currentVacancy: VacancyDetails? = null
    fun checkIsVacancyFavorite() {
        viewModelScope.launch {
            isVacancyFavoriteLiveData.postValue(favoriteInteractor.getVacanciesId().contains(vacancyId))
        }
    }
    fun observeFavoriteState(): LiveData<Boolean> = isVacancyFavoriteLiveData
    private val isVacancyFavoriteLiveData = MutableLiveData<Boolean>(false)
    private val _state = MutableStateFlow<VacancyDetailsScreenState>(VacancyDetailsScreenState.Loading)
    val state: StateFlow<VacancyDetailsScreenState> = _state.asStateFlow()

    init {
        observeQuery(vacancyId)
    }

    private fun observeQuery(vacancyId: String) {
        viewModelScope.launch {
            _state.value = VacancyDetailsScreenState.Loading
            when (val result = detailsInteractor.getVacancyDetail(vacancyId)) {
                is Resource.Success -> {
                    val data = result.data
                    if (data == null) {
                        _state.value = VacancyDetailsScreenState.NothingFound
                    } else {
                        currentVacancy = data.item
                        _state.value = VacancyDetailsScreenState.Content(data.item)
                    }
                }
                is Resource.Loading -> {
                    _state.value = VacancyDetailsScreenState.Loading
                }
                is Resource.Error -> {
                    _state.value = when (result.kind) {
                        ErrorKind.NO_INTERNET -> loadFromCacheOrEmpty(vacancyId)
                        ErrorKind.SERVER -> loadFromCacheOrEmpty(vacancyId)
                    }
                }
            }
        }
    }
    private suspend fun loadFromCacheOrEmpty(vacancyId: String): VacancyDetailsScreenState {
        val cached = favoriteInteractor
            .getVacancyDetailsById(vacancyId) ?: return VacancyDetailsScreenState.NothingFound
        currentVacancy = cached
        return VacancyDetailsScreenState.Content(cached)
    }
    fun shareVacancy() {
        sharingInteractor.shareVacancy(currentVacancy!!.url)
    }
    fun contactByMail() {
        sharingInteractor.sendMail(currentVacancy!!.contactsEmail!!)
    }
    fun callContactPhone(num: String) {
        sharingInteractor.makeCall(num)
    }
    fun onFavoriteClicked() {
        val isFavorite = isVacancyFavoriteLiveData.value ?: false
        isVacancyFavoriteLiveData.value = !isFavorite
        viewModelScope.launch {
            if (isFavorite) {
                favoriteInteractor.deleteVacancyById(vacancyId)
                favoriteInteractor.deleteVacancyDetailsById(vacancyId)
            } else {
                currentVacancy?.let { vacancy ->
                    favoriteInteractor.insertVacancy(convertToVacancyCard(vacancy))
                    favoriteInteractor.saveVacancyDetails(vacancy)
                }
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
