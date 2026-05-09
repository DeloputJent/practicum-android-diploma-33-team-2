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

class FilterFragment  : Fragment() {
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

        viewModel=getViewModel()

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddWorkPlaceFilter.setOnClickListener {

        }

        binding.editWantedSalary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!text.matches("\\d+".toRegex())) {
                    binding.editWantedSalary.removeTextChangedListener(this) // отключаем слушатель
                    binding.editWantedSalary.setText(text.filter { it.isDigit() || it == '-' }) // оставляем только цифры и минус
                    binding.editWantedSalary.addTextChangedListener(this) // возвращаем слушатель
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}

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
            if (hasFocus) {
                if (!binding.editWantedSalary.text.isNullOrEmpty()) {
                    binding.buttonClearSalaryInput.visibility = View.VISIBLE
                    binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Blue))
                } else  {
                    binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Gray))
                }
            } else {
                binding.buttonClearSalaryInput.visibility = View.GONE
                if (!binding.editWantedSalary.text.isNullOrEmpty()) {
                    binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Black))
                } else binding.textHintWantedSalary.setTextColor(resources.getColor(R.color.HH_Gray))
            }
        }

        binding.editWantedSalary.setOnEditorActionListener { _, actionId, _ ->
            val action = actionId == EditorInfo.IME_ACTION_DONE
            if (action)  binding.editWantedSalary.clearFocus()
            false
        }

        binding.buttonClearSalaryInput.setOnClickListener{
            binding.editWantedSalary.setText("")
        }

        binding.textOnlyWithSalaryCheckBox.setOnClickListener {
            viewModel.isCheckedOnlyWithSalary = !viewModel.isCheckedOnlyWithSalary
            viewModel.isOnlyWithSalaryLiveData.value = viewModel.isCheckedOnlyWithSalary

            val drawableResourceId = if (viewModel.isCheckedOnlyWithSalary) {
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
        }

        binding.buttonApplyFilterParameters.setOnClickListener {

        }

        binding.buttonDropFilterParameters.setOnClickListener {
            DropWorkAreaFilter()
            DropIndustryFilter()
        }

    }

    private fun visibilityOfFilterButtons(isVisible: Boolean){
        if (isVisible) {
            binding.buttonApplyFilterParameters.visibility = View.VISIBLE
            binding.buttonDropFilterParameters.visibility = View.VISIBLE
        } else {
            binding.buttonApplyFilterParameters.visibility = View.GONE
            binding.buttonDropFilterParameters.visibility = View.GONE
        }
    }

    private fun SetWorkAreaFilter(location:String){
        binding.textSelectedWorkPlace.text = location
        binding.textHintWorkPlace.visibility = View.GONE
        binding.imageWorkPlaceGoClear.setImageResource(R.drawable.ic_close_24dp)
        binding.viewButtonSelectWorkPlace.visibility = View.VISIBLE
    }

    private fun DropWorkAreaFilter(){
        binding.textSelectedWorkPlace.text = ""
        binding.textHintWorkPlace.visibility = View.VISIBLE
        binding.imageWorkPlaceGoClear.setImageResource(R.drawable.ic_arrow_forward_24dp)
        binding.viewButtonSelectWorkPlace.visibility = View.GONE
    }

    private fun SetIndustryFilter(industryName:String){
        binding.textSelectedIndustry.text = industryName
        binding.textHintIndustry.visibility = View.GONE
        binding.imageIndustryGoClear.setImageResource(R.drawable.ic_close_24dp)
        binding.viewButtonSelectIndustry.visibility = View.VISIBLE
    }

    private fun DropIndustryFilter(){
        binding.textSelectedIndustry.text = ""
        binding.textHintIndustry.visibility = View.VISIBLE
        binding.imageIndustryGoClear.setImageResource(R.drawable.ic_arrow_forward_24dp)
        binding.viewButtonSelectIndustry.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
