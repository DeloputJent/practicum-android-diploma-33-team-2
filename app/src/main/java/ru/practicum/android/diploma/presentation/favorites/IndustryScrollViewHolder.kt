package ru.practicum.android.diploma.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding

class IndustryScrollViewHolder (private val binding: ItemIndustryBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(industryName: String) {
        binding.textIndustryName.text = industryName.trim()
    }

    private val flagOfSelection = binding.buttonSelectThisIndustry

    fun setFlagOn(){
        flagOfSelection.setImageResource(R.drawable.ic_radio_button_on_24dp)
    }

    fun setFlagOff(){
        flagOfSelection.setImageResource(R.drawable.ic_radio_button_off_24dp)
    }

    companion object {
        fun from(parent: ViewGroup): IndustryScrollViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemIndustryBinding.inflate(inflater, parent, false)
            return IndustryScrollViewHolder(binding)
        }
    }
}
