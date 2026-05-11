package ru.practicum.android.diploma.presentation.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry

class IndustryScrollAdapter(
    private val clickListener: (FilterIndustry) -> Unit = {}
) : RecyclerView.Adapter<IndustryScrollViewHolder> () {
    private val industryNames: MutableList<FilterIndustry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryScrollViewHolder =
        IndustryScrollViewHolder.from(parent)

    override fun onBindViewHolder(holder: IndustryScrollViewHolder, position: Int) {
        holder.bind(industryNames[position])
        holder.itemView.setOnClickListener {
            clickListener(industryNames[position])
        }
    }

    fun setIndustryNamesList(industryNames: List<FilterIndustry>) {
        this.industryNames.clear()
        this.industryNames.addAll(industryNames)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return industryNames.size
    }
}
