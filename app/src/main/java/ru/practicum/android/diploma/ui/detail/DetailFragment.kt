package ru.practicum.android.diploma.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.domain.detail.VacancyDetailsScreenState
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails


class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModel()
    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vacancyId = requireArguments().getInt(VACANCY_ID_KEY)

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonShareVacancy.setOnClickListener {        }

        binding.buttonAddToFavorite.setOnClickListener {  }


        fun render(state: VacancyDetailsScreenState) {
            when (state) {
                is VacancyDetailsScreenState.Loading -> showLoading()
                //is VacancyDetailsScreenState.Content -> showVacancy()
                is VacancyDetailsScreenState.ServerError -> showServerDidNotRespond()
                is VacancyDetailsScreenState.NothingFound -> showNothingFoundMessage()
                else -> {showLoading()}
            }
        }
    }

    fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility = View.GONE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }
    fun showVacancy(vacancy: VacancyDetails) {
        binding.apply {
            textVacancyName.text=vacancy.name
            textSalary.text=vacancy.salary
            textEmployerName.text=vacancy.employerName
            textAddress.text=vacancy.name
            textExperience.text=vacancy.experience
            textSchedule.text=vacancy.schedule
            textContactsName.text=vacancy.contactsName
            textContactsEmail.text=vacancy.name
            //textPhones.text=vacancy.name
            textDescription.text=HtmlCompat.fromHtml(
                vacancy.description  ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            textSkills.text=vacancy.skills
        }
        Glide.with(this)
            .load(vacancy.employerLogo)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValueCompat.dpToPx(12f, this.resources.displayMetrics).toInt()
                )
            )
            .placeholder(R.drawable.ic_placeholder_logo_48dp)
            .into(binding.imageLogo)


        binding.apply {
            progressBar.visibility = View.VISIBLE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility = View.GONE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }
    fun showServerDidNotRespond() {
        binding.apply {
            progressBar.visibility = View.GONE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility =  View.VISIBLE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }
    fun showNothingFoundMessage() {
        binding.apply {
            progressBar.visibility = View.GONE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.VISIBLE
            layoutServerDidntRespond.visibility =  View.GONE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val VACANCY_ID_KEY = "current_vacancy"

        fun createArgs(vacancyId: Int): Bundle =
            bundleOf(
                VACANCY_ID_KEY to vacancyId,
            )
    }

}
