package ru.practicum.android.diploma.presentation.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry

class IndustryScrollViewHolder(private val binding: ItemIndustryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(industryName: FilterIndustry) {
        binding.textIndustryName.text = industryName.name?.trim()
        setFlag(industryName.flagOfSelection)
    }

    fun setFlag(flagOfSelection : Boolean) {
        if(flagOfSelection) binding.buttonSelectThisIndustry.setImageResource(R.drawable.ic_radio_button_on_24dp)
        else binding.buttonSelectThisIndustry.setImageResource(R.drawable.ic_radio_button_off_24dp)
    }

    companion object {
        fun from(parent: ViewGroup): IndustryScrollViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemIndustryBinding.inflate(inflater, parent, false)
            return IndustryScrollViewHolder(binding)
        }
    }
}
