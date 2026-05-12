package ru.practicum.android.diploma.domain.detail.api

interface IntentProvider {
    fun sendMail(url: String)
    fun makeCall(num: String)
    fun shareVacancy(sharedUrl: String)
}
