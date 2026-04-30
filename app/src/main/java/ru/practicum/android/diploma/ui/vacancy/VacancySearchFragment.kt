package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R

class VacancySearchFragment : Fragment(R.layout.fragment_vacancy_search) {

    private val viewModel: VacancySearchViewModel by viewModel()
    private val vacancyAdapter = VacancyAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonFilter = view.findViewById<View>(R.id.buttonFilter)
        val editInput = view.findViewById<EditText>(R.id.editInputToSearch)
        val inputIcon = view.findViewById<ImageView>(R.id.imageEditSign)
        val amountText = view.findViewById<TextView>(R.id.textAmountOfFounded)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerVacancies)
        val blank = view.findViewById<View>(R.id.layoutBlankSearch)
        val progress = view.findViewById<View>(R.id.progressBar)
        val noInternet = view.findViewById<View>(R.id.layoutNoInternet)
        val noVacancies = view.findViewById<View>(R.id.layoutNoVacancies)

        recycler.adapter = vacancyAdapter

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
                        }
                        VacancySearchUiState.Loading -> {
                            progress.visibility = View.VISIBLE
                            amountText.visibility = View.GONE
                            recycler.visibility = View.GONE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                        }
                        is VacancySearchUiState.Content -> {
                            progress.visibility = View.GONE
                            amountText.visibility = View.VISIBLE
                            recycler.visibility = View.VISIBLE
                            blank.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noVacancies.visibility = View.GONE
                            amountText.text = resources.getQuantityString(
                                R.plurals.search_founded_n_vacancies,
                                state.found,
                                state.found
                            )
                            vacancyAdapter.submitList(state.items)
                        }
                    }
                }
            }
        }

        buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_vacancySearchFragment_to_filterFragment)
        }
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
}
