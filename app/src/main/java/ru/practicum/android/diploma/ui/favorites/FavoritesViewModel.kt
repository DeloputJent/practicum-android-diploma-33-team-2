package ru.practicum.android.diploma.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.api.FavoriteVacancyInteractor

class FavoritesViewModel(
    private val interactor: FavoriteVacancyInteractor,
) : ViewModel() {
    private val _state = MutableStateFlow<FavouritesScreenState>(FavouritesScreenState.Loading)
    val state: StateFlow<FavouritesScreenState> = _state.asStateFlow()
    private var loadJob: Job? = null
    fun load() {
        if (loadJob != null) return
        loadJob = viewModelScope.launch {
            _state.value = FavouritesScreenState.Loading
            interactor.getVacancies().collect { list ->
                if (list.isNullOrEmpty()) {
                    _state.value = FavouritesScreenState.NothingFound
                } else {
                    _state.value = FavouritesScreenState.Content(list)
                }
            }
        }
    }
}
