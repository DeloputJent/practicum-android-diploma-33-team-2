package ru.practicum.android.diploma.ui.vacancy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.search.models.Salary
import ru.practicum.android.diploma.domain.search.models.VacancyShort
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class VacancyAdapter(
    private val clickListener: VacancyClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyShort)
    }

    private val items = mutableListOf<VacancyShort>()
    private var showLoading = false

    fun showLoading(show: Boolean) {
        if (showLoading != show) {
            showLoading = show
            notifyDataSetChanged()
        }
    }

    fun submitList(newItems: List<VacancyShort>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (showLoading && position == itemCount - 1) TYPE_LOADING else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_LOADING) {
            val view = inflater.inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        } else {
            val binding = ItemVacancyBinding.inflate(inflater, parent, false)
            VacancyViewHolder(binding, clickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VacancyViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size + if (showLoading) 1 else 0

    private class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class VacancyViewHolder(
        private val binding: ItemVacancyBinding,
        private val clickListener: VacancyClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VacancyShort) {
            val title = if (item.city.isNullOrBlank()) item.name else "${item.name}, ${item.city}"
            binding.textVacancyName.text = title
            binding.textVacancyCompany.text = item.company.orEmpty()
            binding.textVacancySalary.text = formatSalary(item.salary, binding)
            binding.root.setOnClickListener { clickListener.onVacancyClick(item) }

            loadLogo(item.logo, binding.imageVacancyLogo)
        }

        private fun loadLogo(rawUrl: String?, imageView: ImageView) {
            Glide.with(binding.imageVacancyLogo)
                .load(rawUrl)
                .placeholder(android.R.color.transparent)
                .error(android.R.color.transparent)
                .fallback(android.R.color.transparent)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }

        private fun formatSalary(salary: Salary?, binding: ItemVacancyBinding): String {
            if (salary == null) {
                return binding.root.context.getString(R.string.search_salary_not_specified)
            }
            val from = salary.from
            val to = salary.to
            val currency = salary.currency?.takeIf { it.isNotBlank() }?.let { " $it" }.orEmpty()
            return when {
                from != null && to != null -> "от ${formatNumber(from)} до ${formatNumber(to)}$currency"
                from != null -> "от ${formatNumber(from)}$currency"
                to != null -> "до ${formatNumber(to)}$currency"
                else -> binding.root.context.getString(R.string.search_salary_not_specified)
            }
        }

        private fun formatNumber(value: Int): String {
            val symbols = DecimalFormatSymbols(Locale("ru", "RU"))
            return DecimalFormat("#,###", symbols).format(value).replace(",", " ")
        }

    }

    private companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }
}
