package ru.practicum.android.diploma.domain.detail.impl

import ru.practicum.android.diploma.domain.detail.api.IntentProvider
import ru.practicum.android.diploma.domain.detail.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: IntentProvider,
) : SharingInteractor {
    override fun sendMail(url: String) {
        externalNavigator.sendMail(url)
    }

    override fun makeCall(num: String) {
        externalNavigator.makeCall(num)
    }

    override fun shareVacancy(sharedUrl: String) {
        externalNavigator
    }
}
