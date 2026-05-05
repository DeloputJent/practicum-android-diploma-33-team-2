package ru.practicum.android.diploma.ui.vacancy

import android.view.LayoutInflater
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
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyShort)
    }

    private val items = mutableListOf<VacancyShort>()

    fun submitList(newItems: List<VacancyShort>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVacancyBinding.inflate(inflater, parent, false)
        return VacancyViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

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
            val logoUrl = normalizeLogoUrl(rawUrl)
            Glide.with(binding.imageVacancyLogo)
                .load(logoUrl)
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

        private fun normalizeLogoUrl(url: String?): String? {
            if (url.isNullOrBlank()) return null
            return if (url.startsWith("//")) "https:$url" else url
        }
    }
}
