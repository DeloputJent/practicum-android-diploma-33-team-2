package ru.practicum.android.diploma.ui.filter.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.presentation.favorites.IndustryScrollAdapter

class IndustryFragment : Fragment() {
    private lateinit var viewModel: IndustryViewModel
    private lateinit var recyclerView : RecyclerView

    private lateinit var industryNamesAdapter : IndustryScrollAdapter
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=getViewModel()

        recyclerView = binding.industryRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        industryNamesAdapter = IndustryScrollAdapter(
            clickListener = { industryName ->

            }
        )

        recyclerView.adapter = industryNamesAdapter

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

