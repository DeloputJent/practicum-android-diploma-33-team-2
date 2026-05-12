package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
import ru.practicum.android.diploma.domain.search.models.VacancyShort
import ru.practicum.android.diploma.ui.details.DetailFragment

class VacancySearchFragment : Fragment(R.layout.fragment_vacancy_search) {

    private val viewModel: VacancySearchViewModel by viewModel()
    private val vacancyClickListener = object : VacancyAdapter.VacancyClickListener {
        override fun onVacancyClick(vacancy: VacancyShort) {
            openVacancyDetails(vacancy.id)
        }
    }
    private val vacancyAdapter = VacancyAdapter(vacancyClickListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonFilter = view.findViewById<ImageButton>(R.id.buttonFilter)
        val editInput = view.findViewById<EditText>(R.id.editInputToSearch)
        val inputIcon = view.findViewById<ImageView>(R.id.imageEditSign)
        val amountText = view.findViewById<TextView>(R.id.textAmountOfFounded)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerVacancies)
        val blank = view.findViewById<View>(R.id.layoutBlankSearch)
        val progress = view.findViewById<View>(R.id.progressBar)
        val noInternet = view.findViewById<View>(R.id.layoutNoInternet)
        val noVacancies = view.findViewById<View>(R.id.layoutNoVacancies)
        val serverError = view.findViewById<View>(R.id.layoutServerError)

        recycler.adapter = vacancyAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getStoragedFilterSettings()
        viewModel.observeFilterSettingsState().observe(viewLifecycleOwner) {
            //editInput.setText(it.searchField)
        }

        viewModel.observeFilterSettingsState().observe(viewLifecycleOwner) {
            updateFilterIcon(!it.isSettingsEmpty(), buttonFilter)
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pos = layoutManager.findLastVisibleItemPosition()
                    val itemsCount = vacancyAdapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })

        editInput.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty()
            viewModel.onQueryChanged(value)
            updateInputIcon(value.isNotBlank(), inputIcon, editInput)
        }

        updateInputIcon(false, inputIcon, editInput)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        VacancySearchUiState.Initial -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.GONE
                            recycler.visibility = View.GONE
                            blank.visibility = View.VISIBLE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                            serverError.visibility = View.GONE
                            vacancyAdapter.showLoading(false)
                        }
                        VacancySearchUiState.Loading -> {
                            progress.visibility = View.VISIBLE
                            amountText.visibility = View.GONE
                            recycler.visibility = View.GONE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                            serverError.visibility = View.GONE
                            vacancyAdapter.showLoading(false)
                        }
                        is VacancySearchUiState.Content -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.VISIBLE
                            recycler.visibility = View.VISIBLE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                            serverError.visibility = View.GONE
                            amountText.text = resources.getQuantityString(
                                R.plurals.search_founded_n_vacancies,
                                state.found,
                                state.found
                            )
                            vacancyAdapter.submitList(state.items)
                        }
                        VacancySearchUiState.Empty -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.VISIBLE
                            amountText.setText(R.string.search_no_vacancies)
                            recycler.visibility = View.GONE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.VISIBLE
                            serverError.visibility = View.GONE
                            vacancyAdapter.showLoading(false)
                        }
                        VacancySearchUiState.NoInternet -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.GONE
                            recycler.visibility = View.GONE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.VISIBLE
                            noVacancies.visibility = View.GONE
                            serverError.visibility = View.GONE
                            vacancyAdapter.showLoading(false)
                        }
                        VacancySearchUiState.ServerError -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.GONE
                            recycler.visibility = View.GONE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                            serverError.visibility = View.VISIBLE
                            vacancyAdapter.showLoading(false)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isNextPageLoading.collect { isLoading ->
                    vacancyAdapter.showLoading(isLoading)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toast.collect { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonFilter.setOnClickListener {
            viewModel.saveSearchToStorage(editInput.text.toString())
            findNavController().navigate(R.id.action_vacancySearchFragment_to_filterFragment)
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        findNavController().navigate(
            R.id.action_vacancySearchFragment_to_detailFragment,
            DetailFragment.createArgs(vacancyId),
        )
    }

    private fun updateInputIcon(
        hasQuery: Boolean,
        inputIcon: ImageView,
        editInput: EditText,
    ) {
        if (hasQuery) {
            inputIcon.setImageResource(R.drawable.ic_close_24dp)
            inputIcon.setOnClickListener {
                editInput.text?.clear()
                viewModel.onQueryChanged("")
            }
        } else {
            inputIcon.setImageResource(R.drawable.ic_search_24dp)
            inputIcon.setOnClickListener(null)
        }
    }

    private fun updateFilterIcon(
        filterSet: Boolean,
        buttonFilter: ImageButton
    ) {
        Log.d("set", "SettingsEmpty=$filterSet")
        if (filterSet) {
            buttonFilter.setImageResource(R.drawable.ic_filter_on_24dp)
        } else {
            buttonFilter.setImageResource(R.drawable.ic_filter_off_24dp)
        }
    }
}
