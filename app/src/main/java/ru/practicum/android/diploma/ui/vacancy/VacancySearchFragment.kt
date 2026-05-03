package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class VacancySearchFragment : Fragment(R.layout.fragment_vacancy_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.buttonFilter).setOnClickListener {
            findNavController().navigate(R.id.action_vacancySearchFragment_to_filterFragment)
        }
    }
}
