package ru.practicum.android.diploma.data.details

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.detail.api.IntentProvider

class ExternalNavigator(private val context: Context) : IntentProvider {
    override fun sendMail(url: String) {
        val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$url".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(sendIntent)
    }

    override fun makeCall(num: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$num".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(callIntent)
    }

    override fun shareVacancy(sharedUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.vacancy_email_title))
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl)
        context.startActivity(
            Intent.createChooser(shareIntent, context.getString(R.string.vacancy_share_with_help))
                .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        )
    }
}
