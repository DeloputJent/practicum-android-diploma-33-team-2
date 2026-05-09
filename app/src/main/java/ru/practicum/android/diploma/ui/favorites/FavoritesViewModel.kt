package ru.practicum.android.diploma.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class FavoritesViewModel(
    private val interactor: FavoriteVacancyInteractor,
) : ViewModel() {

    private val _vacancies = MutableStateFlow<List<VacancyCard>>(emptyList())
    val vacancies: StateFlow<List<VacancyCard>> = _vacancies.asStateFlow()

    private var loadJob: Job? = null

    fun load() {
        if (loadJob != null) return
        loadJob = viewModelScope.launch {
            interactor.getVacancies().collect { list ->
                _vacancies.value = list
            }
        }
    }
}
