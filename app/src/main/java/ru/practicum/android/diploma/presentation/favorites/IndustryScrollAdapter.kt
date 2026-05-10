package ru.practicum.android.diploma.presentation.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class IndustryScrollAdapter(
    private val clickListener: (String) -> Unit = {}
) : RecyclerView.Adapter<IndustryScrollViewHolder> () {
    private val industryNames: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryScrollViewHolder =
        IndustryScrollViewHolder.from(parent)

    override fun onBindViewHolder(holder: IndustryScrollViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener(industryNames[position])
        }
    }

    fun setIndustryNamesList(industryNames: List<String>) {
        this.industryNames.clear()
        this.industryNames.addAll(industryNames)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return industryNames.size
    }
}
