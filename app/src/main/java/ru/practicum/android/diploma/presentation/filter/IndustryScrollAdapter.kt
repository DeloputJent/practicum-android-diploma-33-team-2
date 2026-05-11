package ru.practicum.android.diploma.presentation.filter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.filter.models.FilterIndustry

class IndustryScrollAdapter(
    private val clickListener: (FilterIndustry) -> Unit = {}
) : RecyclerView.Adapter<IndustryScrollViewHolder> () {
    private val filterIndustryList: MutableList<FilterIndustry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryScrollViewHolder =
        IndustryScrollViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: IndustryScrollViewHolder, position: Int) {
        holder.bind(filterIndustryList[position])
        holder.itemView.setOnClickListener {
            filterIndustryList[position].flagOfSelection=!filterIndustryList[position].flagOfSelection
            holder.setFlag(filterIndustryList[position].flagOfSelection)
            val selectedIndustry = listOf(filterIndustryList[position])
            setIndustryNamesList(selectedIndustry)
            //clickListener(selectedIndustry)
        }
    }

    fun setIndustryNamesList(industryNames: List<FilterIndustry>) {
        this.filterIndustryList.clear()
        this.filterIndustryList.addAll(industryNames)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filterIndustryList.size
    }
}
