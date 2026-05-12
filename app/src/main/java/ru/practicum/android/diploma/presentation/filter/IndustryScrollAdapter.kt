package ru.practicum.android.diploma.presentation.filter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry

class IndustryScrollAdapter(
    private val clickListener: (FilterIndustry) -> Unit = {}
) : RecyclerView.Adapter<IndustryScrollViewHolder> () {
    private val industryList: MutableList<FilterIndustry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryScrollViewHolder =
        IndustryScrollViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: IndustryScrollViewHolder, position: Int) {
        holder.bind(industryList[position])
        holder.itemView.setOnClickListener {
            industryList.forEach { it.flagOfSelection = false }
            industryList[position].flagOfSelection=!industryList[position].flagOfSelection
            holder.setFlag(industryList[position].flagOfSelection)
            setIndustryNamesList(industryList.toList())
            clickListener(industryList[position])
        }
    }

    fun setIndustryNamesList(industryNames: List<FilterIndustry>) {
        this.industryList.clear()
        this.industryList.addAll(industryNames)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return industryList.size
    }
}
