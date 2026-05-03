package ru.practicum.android.diploma.presentation.details

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails

class PhoneListAdapter(
    private val clickListener: (VacancyDetails.Phone) -> Unit = {}
) : RecyclerView.Adapter<PhoneListViewHolder> () {
    private val phones: MutableList<VacancyDetails.Phone> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneListViewHolder =
        PhoneListViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: PhoneListViewHolder, position: Int) {
        holder.bind(phones[position])
        holder.itemView.setOnClickListener { clickListener(phones[position])
        }
    }

    fun setPhonesList(phones: List<VacancyDetails.Phone>) {
        this.phones.clear()
        this.phones.addAll(phones)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return phones.size
    }
}
