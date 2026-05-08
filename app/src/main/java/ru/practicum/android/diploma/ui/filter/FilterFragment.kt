package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddWorkPlaceFilter.setOnClickListener {

        }





    }

    private fun SetWorkAreaFilter(location:String){
        binding.textHintWorkPlace.visibility = View.GONE
        binding.imageWorkPlaceGoClear.setImageResource(R.drawable.ic_close_24dp)
        binding.viewButtonSelectWorkPlace.visibility = View.VISIBLE
        binding.textSelectedWorkPlace.text = location
    }

    private fun DropWorkAreaFilter(){
        binding.textHintWorkPlace.visibility = View.VISIBLE
        binding.imageWorkPlaceGoClear.setImageResource(R.drawable.ic_arrow_forward_24dp)
        binding.viewButtonSelectWorkPlace.visibility = View.GONE
        binding.textSelectedWorkPlace.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    }
