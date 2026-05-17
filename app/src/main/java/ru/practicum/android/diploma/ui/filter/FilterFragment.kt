package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.filter.models.FilterSettings

class FilterFragment : Fragment() {
    private lateinit var viewModel: FilterViewModel
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel()
        viewModel.getStoragedFilterSettings()

        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack(R.id.vacancySearchFragment, false)
        }
        viewModel.observeFilterSettingsState().observe(viewLifecycleOwner) {
            renderSettings(it)
        }

        binding.buttonAddIndustryFilter.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }

        binding.editWantedSalary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!text.matches("\\d+".toRegex())) {
                    binding.editWantedSalary.removeTextChangedListener(this)
                    binding.editWantedSalary.setText(text.filter { it.isDigit() || it == '-' })
                    binding.editWantedSalary.addTextChangedListener(this)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    binding.buttonClearSalaryInput.visibility = View.VISIBLE
                    binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Blue))
                } else {
                    binding.buttonClearSalaryInput.visibility = View.GONE
                    binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Gray))
                }
            }
        })

        binding.editWantedSalary.setOnFocusChangeListener { view, hasFocus ->
            isWantedSalaryFieldHasFocus(hasFocus)
        }

        binding.editWantedSalary.setOnEditorActionListener { _, actionId, _ ->
            val action = actionId == EditorInfo.IME_ACTION_DONE
            if (action) {
                binding.editWantedSalary.clearFocus()
                binding.editWantedSalary.text.toString().let {
                    viewModel.filterSettings = viewModel
                        .filterSettings
                        .copy(salary = if (it.isNotEmpty()) it.toInt() else null)
                    viewModel.updateFilterSettingsLiveData()
                }
            }
            false
        }

        binding.buttonClearSalaryInput.setOnClickListener {
            binding.editWantedSalary.text.toString().let {
                viewModel.filterSettings = viewModel
                    .filterSettings
                    .copy(salary = null)
                viewModel.updateFilterSettingsLiveData()
            }
        }

        binding.textOnlyWithSalaryCheckBox.setOnClickListener {
            val onlyWithSalary = !viewModel.filterSettings.onlyWithSalary
            viewModel.filterSettings = viewModel.filterSettings.copy(
                onlyWithSalary = onlyWithSalary
            )
            viewModel.updateFilterSettingsLiveData()
        }

        binding.buttonApplyFilterParameters.setOnClickListener {
            binding.editWantedSalary.text.toString().let {
                viewModel.filterSettings = viewModel.filterSettings.copy(
                    salary = if (it.isNotEmpty()) it.toInt() else null
                )
            }
            viewModel.updateFilterSettingsLiveData()
            findNavController().getBackStackEntry(R.id.vacancySearchFragment).savedStateHandle["apply_filters"] = true
            findNavController().popBackStack(R.id.vacancySearchFragment, false)
        }

        binding.buttonDropFilterParameters.setOnClickListener {
            viewModel.clearFilterSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStoragedFilterSettings()
    }

    private fun renderSettings(settings: FilterSettings) {
        if (settings.industryName != null) {
            setIndustryFilter(settings.industryName)
        } else {
            dropIndustryFilter()
        }
        if (settings.salary != null) {
            binding.editWantedSalary.setText(settings.salary.toString())
        } else {
            binding.editWantedSalary.setText("")
        }
        val drawableResourceId = if (settings.onlyWithSalary) {
            R.drawable.ic_check_box_on__24dp
        } else {
            R.drawable.ic_check_box_off__24dp
        }
        binding.textOnlyWithSalaryCheckBox.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ContextCompat.getDrawable(requireContext(), drawableResourceId),
            null
        )
        visibilityOfFilterButtons(!settings.isSettingsEmpty())
    }

    private fun isWantedSalaryFieldHasFocus(hasFocus: Boolean) {
        if (hasFocus) {
            if (!binding.editWantedSalary.text.isNullOrEmpty()) {
                binding.buttonClearSalaryInput.visibility = View.VISIBLE
                binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Blue))
            } else {
                binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Gray))
            }
        } else {
            binding.buttonClearSalaryInput.visibility = View.GONE
            if (!binding.editWantedSalary.text.isNullOrEmpty()) {
                binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Black))
            } else {
                binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Gray))
            }
        }
    }

    private fun visibilityOfFilterButtons(isVisible: Boolean) {
        if (isVisible) {
            binding.buttonApplyFilterParameters.visibility = View.VISIBLE
            binding.buttonDropFilterParameters.visibility = View.VISIBLE
        } else {
            binding.buttonApplyFilterParameters.visibility = View.GONE
            binding.buttonDropFilterParameters.visibility = View.GONE
        }
    }

    private fun setIndustryFilter(industryName: String?) {
        binding.apply {
            textSelectedIndustry.text = industryName
            textHintSelectIndustry.visibility = View.GONE
            imageIndustryGoClear.setImageResource(R.drawable.ic_close_24dp)
            viewButtonSelectIndustry.visibility = View.VISIBLE
            binding.imageIndustryGoClear.setOnClickListener {
                viewModel.filterSettings = viewModel.filterSettings.copy(
                    industryId = null,
                    industryName = null
                )
                viewModel.updateFilterSettingsLiveData()
            }
        }
    }

    private fun dropIndustryFilter() {
        binding.apply {
            textSelectedIndustry.text = ""
            textHintSelectIndustry.visibility = View.VISIBLE
            imageIndustryGoClear.setImageResource(R.drawable.ic_arrow_forward_24dp)
            viewButtonSelectIndustry.visibility = View.GONE
            binding.imageIndustryGoClear.setOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
