package ru.practicum.android.diploma.ui.filter.industry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.presentation.filter.IndustryScrollAdapter

class IndustryFragment : Fragment() {
    private val viewModel by viewModel<IndustryViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var industryNamesAdapter: IndustryScrollAdapter
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.industryRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        industryNamesAdapter = IndustryScrollAdapter(
            clickListener = { industryName ->
                run {
                    binding.buttonApplyIndustryFilter.visibility = View.VISIBLE
                    viewModel.chooseSelectedIndustry(industryName)
                }
            }
        )
        recyclerView.adapter = industryNamesAdapter

        binding.editWantedIndustry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
               viewModel.observeFilteredScroll(p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> setScreenState(state) }
            }
        }

        binding.editWantedIndustry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val fieldHasText = !p0.isNullOrEmpty()
                setClearButtonOnField(fieldHasText)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        binding.buttonApplyIndustryFilter.setOnClickListener {
            viewModel.saveSelectedIndustry()
            findNavController().navigate(R.id.action_industryFragment_to_filterFragment)
        }

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setScreenState(state:IndustryListScreenState){
        when (state) {
            IndustryListScreenState.Loading -> {
                binding.apply {
                    layoutNoResponse.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
            is IndustryListScreenState.Content -> {
                binding.apply {
                    layoutNoResponse.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                industryNamesAdapter.setIndustryNamesList(state.industryList)
            }
            IndustryListScreenState.ServerError -> {
                binding.apply {
                    layoutNoResponse.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun setClearButtonOnField(fieldHasText: Boolean) {
        if (fieldHasText) {
            binding.buttonClearIndustryInput.setImageResource(R.drawable.ic_close_24dp)
            binding.buttonClearIndustryInput.setOnClickListener {
                binding.editWantedIndustry.text = null
            }
        } else {
            binding.buttonClearIndustryInput.setImageResource(R.drawable.ic_search_24dp)
            binding.buttonClearIndustryInput.setOnClickListener { null }
        }
    }
}
