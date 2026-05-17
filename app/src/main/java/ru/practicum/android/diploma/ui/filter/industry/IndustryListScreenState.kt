package ru.practicum.android.diploma.ui.filter.industry

import ru.practicum.android.diploma.domain.filter.models.FilterIndustry

sealed interface IndustryListScreenState {
    object Loading : IndustryListScreenState
    object ServerError : IndustryListScreenState
    data class Content(val industryList: List<FilterIndustry>) : IndustryListScreenState
}
