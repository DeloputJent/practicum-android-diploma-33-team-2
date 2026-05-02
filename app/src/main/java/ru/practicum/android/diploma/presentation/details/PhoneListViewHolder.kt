package com.practicum.playlistmaker.search.ui.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.PhoneFrameViewBinding
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

class PhoneListViewHolder (
    private val binding: PhoneFrameViewBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(phone: VacancyDetails.Phone) {
        binding.apply {
            if (phone.comment!=null) textPhoneComment.text=phone.comment
            else textPhoneComment.visibility = View.GONE
            textPhoneNum.text=phone.formatted
        }
    }

    companion object {
        fun from(parent: ViewGroup): PhoneListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PhoneFrameViewBinding.inflate(inflater, parent, false)
            return PhoneListViewHolder(binding)
        }
    }
}
