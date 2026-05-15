package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.favorites.models.VacancyCard
import ru.practicum.android.diploma.ui.details.DetailFragment

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel by viewModel<FavoritesViewModel>()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val adapterClickListener = object : FavoritesAdapter.FavoriteClickListener {
        override fun onVacancyClick(vacancy: VacancyCard) {
            findNavController().navigate(
                R.id.action_favoritesFragment_to_detailFragment,
                DetailFragment.createArgs(vacancy.id),
            )
        }
    }
    private val adapter = FavoritesAdapter(adapterClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerFavourites
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.load()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> setScreenState(state) }
            }
        }
    }

    private fun setScreenState(state: FavouritesScreenState) {
        when (state) {
            FavouritesScreenState.Loading -> {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    layoutLoadError.visibility = View.GONE
                    layoutNoVacancy.visibility = View.GONE
                    recyclerFavourites.visibility = View.GONE
                }
            }
            is FavouritesScreenState.Content -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    layoutLoadError.visibility = View.GONE
                    layoutNoVacancy.visibility = View.GONE
                    recyclerFavourites.visibility = View.VISIBLE
                }
                adapter.submitList(state.listOfFavourites)
            }
            FavouritesScreenState.NothingFound -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    layoutLoadError.visibility = View.GONE
                    layoutNoVacancy.visibility = View.VISIBLE
                    recyclerFavourites.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
