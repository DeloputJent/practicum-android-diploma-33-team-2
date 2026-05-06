package ru.practicum.android.diploma.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.domain.detail.model.VacancyDetails
import ru.practicum.android.diploma.presentation.details.PhoneListAdapter

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var phoneListAdapter: PhoneListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vacancyId = requireArguments().getString(VACANCY_ID_KEY)
            ?: error("vacancyId argument is required")
        viewModel = getViewModel(parameters = { parametersOf(vacancyId) })

        viewModel.checkIsVacancyFavorite()
        val recyclerView = binding.textPhones
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        phoneListAdapter = PhoneListAdapter(
            clickListener = {
                    phone -> viewModel.callContactPhone(phone.formatted)
            }
        )

        binding.textContactsEmail.setOnClickListener {
            viewModel.contactByMail()
        }

        recyclerView.adapter = phoneListAdapter

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonShareVacancy.setOnClickListener { viewModel.shareVacancy() }

        binding.buttonAddToFavorite.setOnClickListener { viewModel.onFavoriteClicked() }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            setFavoriteButton(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        VacancyDetailsScreenState.Loading -> {
                            showLoading()
                        }
                        is VacancyDetailsScreenState.Content -> {
                            showVacancy(state.vacancy)
                            viewModel.checkIsVacancyFavorite()
                        }
                        is VacancyDetailsScreenState.ServerError -> {
                            showServerDidNotRespond()
                        }
                        is VacancyDetailsScreenState.NothingFound -> {
                            showNothingFoundMessage()
                        }
                    }
                }
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
        setVacancyNameAndSalary(vacancy)

        setEmployersRequirements(vacancy)

        setCompanyInfo(vacancy)

        setEmployerContacts(vacancy)

        setVacancyDescription(vacancy)

        binding.apply {
            progressBar.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility = View.GONE
            layoutVacancyDetail.visibility = View.VISIBLE
            buttonShareVacancy.visibility = View.VISIBLE
            buttonAddToFavorite.visibility = View.VISIBLE
        }
    }

    private fun setCompanyInfo(vacancy: VacancyDetails) {
        binding.apply {
            textEmployerName.text = vacancy.employerName
            if (!vacancy.address.isNullOrEmpty()) {
                textAddress.text = vacancy.address
            } else {
                textAddress.text = vacancy.areaName
            }
            Glide.with(requireContext())
                .load(vacancy.employerLogo)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(
                            CORNER_RADIUS,
                            requireContext().resources.displayMetrics
                        )
                            .toInt()
                    )
                )
                .placeholder(R.drawable.ic_placeholder_logo_48dp)
                .into(binding.imageLogo)
        }
    }

    private fun setVacancyNameAndSalary(vacancy: VacancyDetails) {
        binding.apply {
            textVacancyName.text = vacancy.name
            if (vacancy.salary.isNotEmpty()) {
                textSalary.text = vacancy.salary
            } else {
                textSalary.visibility = View.GONE
            }
        }
    }
    private fun setEmployerContacts(vacancy: VacancyDetails) {
        binding.apply {
            if (
                vacancy.contactsName.isNullOrEmpty()
                and vacancy.contactsEmail.isNullOrEmpty()
                and vacancy.phones.isNullOrEmpty()
            ) {
                textContactsTitle.visibility = View.GONE
                textContactsName.visibility = View.GONE
                textContactsEmail.visibility = View.GONE
                textEmailTitle.visibility = View.GONE
                textPhones.visibility = View.GONE
                textPhonesTitle.visibility = View.GONE
            } else {
                if (!vacancy.contactsName.isNullOrEmpty()) {
                    textContactsName.text = vacancy.contactsName
                } else {
                    textContactsName.visibility = View.GONE
                }
                if (!vacancy.contactsEmail.isNullOrEmpty()) {
                    textContactsEmail.text = vacancy.contactsEmail
                } else {
                    textContactsEmail.visibility = View.GONE
                    textEmailTitle.visibility = View.GONE
                }
                if (!vacancy.phones.isNullOrEmpty()) {
                    phoneListAdapter.setPhonesList(vacancy.phones)
                } else {
                    textPhones.visibility = View.GONE
                    textPhonesTitle.visibility = View.GONE
                }
            }
        }
    }

    private fun setEmployersRequirements(vacancy: VacancyDetails) {
        binding.apply {
            if (!vacancy.experience.isNullOrEmpty()) {
                textExperience.text = vacancy.experience
            } else {
                textExperienceTitle.visibility = View.GONE
                textExperience.visibility = View.GONE
            }
            if (!vacancy.scheduleAndEmployment.isNullOrEmpty()) {
                textEmploymentAndSchedule.text = vacancy.scheduleAndEmployment
            } else {
                textEmploymentAndSchedule.visibility = View.GONE
            }
        }
    }
    private fun setVacancyDescription(vacancy: VacancyDetails) {
        binding.apply {
            textDescription.text = HtmlCompat.fromHtml(
                vacancy.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            if (!vacancy.skills.isNullOrEmpty()) {
                textSkills.text = vacancy.skills
            } else {
                textSkills.visibility = View.GONE
                textSkillsTitle.visibility = View.GONE
            }
        }
    }
    fun showServerDidNotRespond() {
        binding.apply {
            progressBar.visibility = View.GONE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.GONE
            layoutServerDidntRespond.visibility = View.VISIBLE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }
    fun showNothingFoundMessage() {
        binding.apply {
            progressBar.visibility = View.GONE
            layoutVacancyDetail.visibility = View.GONE
            layoutNoVacancy.visibility = View.VISIBLE
            layoutServerDidntRespond.visibility = View.GONE
            buttonShareVacancy.visibility = View.GONE
            buttonAddToFavorite.visibility = View.GONE
        }
    }
    private fun setFavoriteButton(isTrackFavorite: Boolean) {
        if (isTrackFavorite) {
            binding.buttonAddToFavorite.setImageResource(R.drawable.ic_favorites_on_24dp)
        } else {
            binding.buttonAddToFavorite.setImageResource(R.drawable.ic_favorites_off__24dp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VACANCY_ID_KEY = "vacancyId"
        private const val CORNER_RADIUS = 12f

        fun createArgs(vacancyId: String): Bundle =
            bundleOf(
                VACANCY_ID_KEY to vacancyId,
            )
    }
}
