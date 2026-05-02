package ru.practicum.android.diploma.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.search.ui.presentation.PhoneListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.domain.detail.VacancyDetailsScreenState
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import androidx.core.net.toUri


class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModel()
    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var phoneListAdapter : PhoneListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vacancyId = requireArguments().getInt(VACANCY_ID_KEY)
        val recyclerView = binding.textPhones
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        phoneListAdapter = PhoneListAdapter(clickListener = { phone ->
            Intent(Intent.ACTION_DIAL).apply {
                data = ("tel:" + phone.formatted).toUri()
            }
        }        )

        binding.textContactsEmail.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = ("mailto:" + binding.textContactsEmail.text).toUri()
            }
        }

        recyclerView.adapter = phoneListAdapter

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonShareVacancy.setOnClickListener {        }

        binding.buttonAddToFavorite.setOnClickListener {  }


        fun render(state: VacancyDetailsScreenState) {
            when (state) {
                is VacancyDetailsScreenState.Loading -> showLoading()
                //is VacancyDetailsScreenState.Content -> showVacancy(vacancy)
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
            if (!vacancy.salary.isNullOrEmpty()) textSalary.text=vacancy.salary
            else textSalary.visibility = View.GONE

            setCompanyInfo(vacancy)

            if (!vacancy.experience.isNullOrEmpty()) textExperience.text=vacancy.experience
            else {
                textExperienceTitle.visibility = View.GONE
                textExperience.visibility = View.GONE
            }
            if (!vacancy.schedule.isNullOrEmpty()) textSchedule.text=vacancy.schedule
            else textSchedule.visibility = View.GONE
            setEmployerContacts(vacancy)
            textDescription.text=HtmlCompat.fromHtml(
                vacancy.description  ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            if (!vacancy.skills.isNullOrEmpty()) textSkills.text=vacancy.skills
            else {
                textSkills.visibility = View.GONE
                textSkillsTitle.visibility = View.GONE
            }
        }



        binding.apply {
            progressBar.visibility = View.VISIBLE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility = View.GONE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }

    private fun setCompanyInfo(vacancy: VacancyDetails) {
        binding.apply {
            textEmployerName.text=vacancy.employerName
            if (!vacancy.address.isNullOrEmpty()) textAddress.text=vacancy.address
            else textAddress.text = vacancy.areaName
            Glide.with(requireContext())
                .load(vacancy.employerLogo)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(
                            12f, requireContext().resources.displayMetrics)
                            .toInt()
                    )
                )
                .placeholder(R.drawable.ic_placeholder_logo_48dp)
                .into(binding.imageLogo)
        }
    }
    private fun setEmployerContacts(vacancy: VacancyDetails) {
        binding.apply {
            if (
                vacancy.contactsName.isNullOrEmpty() and vacancy.contactsEmail.isNullOrEmpty() and vacancy.phones.isNullOrEmpty()
            ) {
                textContactsTitle.visibility = View.GONE
                textContactsName.visibility = View.GONE
                textContactsEmail.visibility = View.GONE
                textEmailTitle.visibility = View.GONE
                textPhones.visibility = View.GONE
                textPhonesTitle.visibility = View.GONE
            } else {
                if (!vacancy.contactsName.isNullOrEmpty()) textContactsName.text=vacancy.contactsName
                else textContactsName.visibility = View.GONE
                if (!vacancy.contactsEmail.isNullOrEmpty()) textContactsEmail.text=vacancy.contactsEmail
                else {
                    textContactsEmail.visibility = View.GONE
                    textEmailTitle.visibility = View.GONE
                }
                if (!vacancy.phones.isNullOrEmpty()) phoneListAdapter.setPhonesList(vacancy.phones)
                else {
                    textPhones.visibility = View.GONE
                    textPhonesTitle.visibility = View.GONE
                }
            }
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
