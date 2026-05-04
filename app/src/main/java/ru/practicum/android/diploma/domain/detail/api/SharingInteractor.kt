package ru.practicum.android.diploma.domain.detail.api

interface SharingInteractor {
    fun sendMail(url: String)
    fun makeCall(num: String)
    fun shareVacancy(sharedUrl: String)
}
