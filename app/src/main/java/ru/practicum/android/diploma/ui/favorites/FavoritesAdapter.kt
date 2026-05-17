package ru.practicum.android.diploma.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.practicum.android.diploma.databinding.ItemFavouriteBinding
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard

class FavoritesAdapter(
    private val clickListener: FavoriteClickListener,
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    fun interface FavoriteClickListener {
        fun onVacancyClick(vacancy: VacancyCard)
    }

    private val items = ArrayList<VacancyCard>()

    fun submitList(list: List<VacancyCard>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFavouriteBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FavoriteViewHolder(
        private val binding: ItemFavouriteBinding,
        private val clickListener: FavoriteClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VacancyCard) {
            binding.textFavouriteVacancyName.text = item.name
            binding.textFavouriteVacancyCompany.text = item.company.orEmpty()
            binding.textVacancySalary.text = item.salary.orEmpty()
            binding.root.setOnClickListener { clickListener.onVacancyClick(item) }
            loadLogo(item.logo, binding.imageFavouriteLogo)
        }

        private fun loadLogo(rawUrl: String?, imageView: ImageView) {
            Glide.with(imageView)
                .load(rawUrl)
                .placeholder(android.R.color.transparent)
                .error(android.R.color.transparent)
                .fallback(android.R.color.transparent)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }
}
