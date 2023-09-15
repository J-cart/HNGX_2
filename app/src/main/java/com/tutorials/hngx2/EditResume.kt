package com.tutorials.hngx2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.tutorials.hngx2.arch.ResumeViewModel
import com.tutorials.hngx2.databinding.FragmentEditResumeBinding
import com.tutorials.hngx2.model.Experience
import com.tutorials.hngx2.model.ResumeProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class EditResume : Fragment() {
    private lateinit var binding: FragmentEditResumeBinding
    private val skillsAdapter by lazy { SkillsAdapter(true) }
    private val expAdapter by lazy { ExperienceAdapter(true) }
    private val viewModel by activityViewModels<ResumeViewModel>()
    private var imageUri:String? = null
    private var startDate:Long? = null
    private var endDate:Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            experienceRv.adapter = expAdapter
            skillsRv.adapter = skillsAdapter

            lifecycleScope.launch {
                viewModel.profileFlow.collect{
                    binding.profileImg.load(it.profileImg){
                        placeholder(R.drawable.ic_launcher_foreground)
                        error(R.drawable.ic_launcher_foreground)
                    }
                    fullNameText.setText(it.fullName)
                    jobTitleText.setText(it.jobTitle)
                    locationText.setText(it.location)
                    profileSummaryText.setText(it.summary)
                    skillsAdapter.submitList(it.skills)
                    expAdapter.submitList(it.experience)

                }
            }


            expAdapter.adapterDeleteClickListener {
                viewModel.removeExperience(it)
            }
            skillsAdapter.adapterDeleteClickListener {
               viewModel.removeSkill(it)
            }

            addSkillBtn.setOnClickListener {
                addSkill(skillInputText.text.toString())
                skillInputText.text?.clear()

            }
            addExpBtn.setOnClickListener {
               addProfileExperience()
            }

            editImgBtn.setOnClickListener {
                imgSelector()
            }
            dateFromText.setOnClickListener {
                dateDialog {
                    startDate = it
                    dateFromText.setText(getShortDate(it))
                }
            }
            dateToText.setOnClickListener {
                dateDialog {
                    endDate = it
                    dateToText.setText(getShortDate(it))
                }
            }

            saveBtn.setOnClickListener {
                lifecycleScope.launch {
                    saveBtn.isVisible= false
                    progressBar.isVisible= true
                    delay(2000L)
                    saveProfile()
                    findNavController().navigateUp()
                }
            }

        }

    }

    private fun saveProfile() {

        binding.apply {
            val resume = ResumeProfile(
                profileImg = imageUri ?:"",
                fullName = fullNameText.text.toString().trim(),
                jobTitle = jobTitleText.text.toString().trim(),
                location = locationText.text.toString().trim(),
                summary = profileSummaryText.text.toString().trim(),
                skills = skillsAdapter.currentList.toList(),
                experience = expAdapter.currentList.toList()
            )
            viewModel.updateProfile(resume)
        }

    }

    private fun addSkill(skill: String) {
        if (skill.trim().isNotEmpty()) {
            viewModel.addSkill(skill)
            return
        }
        Toast.makeText(requireContext(), "Skill input can't be empty", Toast.LENGTH_SHORT).show()
    }

    private fun addExperience(experience: Experience) {
        if (experience.jobTitle.trim().isNotEmpty()) {
            viewModel.addExperience(experience)
            return
        }
        Toast.makeText(requireContext(), "Skill input can't be empty", Toast.LENGTH_SHORT).show()

    }


    private val requestAccountImgPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val resultImageUri = it.data?.data
                resultImageUri?.let { uri ->
                    imageUri = uri.toString()
                    binding.profileImg.load(imageUri){
                        placeholder(R.drawable.ic_launcher_foreground)
                        error(R.drawable.ic_launcher_foreground)
                    }
                }

                return@registerForActivityResult
            }
            Toast.makeText(requireContext(), "Unable to update profile image", Toast.LENGTH_SHORT)
                .show()
        }

    private fun imgSelector() {
        val intent = Intent(
            Intent.ACTION_GET_CONTENT,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "image/*"
        }
        requestAccountImgPicker.launch(intent)

    }

   private fun getShortDate(date: Long): String {
        return SimpleDateFormat("MMM dd,yy", Locale.getDefault()).format(date)
    }

    private fun dateDialog(action:(Long)->Unit){
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = calendar[Calendar.MONTH]

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setEnd(calendar.timeInMillis)
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder)
                .build()
        datePicker.addOnPositiveButtonClickListener {
            action(it)
        }
        datePicker.addOnCancelListener {
            it.dismiss()
        }
        datePicker.show(this@EditResume.childFragmentManager,"Tag")
    }

    private fun checkIfDateIsPresent(date: Long):Boolean{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

         val tCalendar = Calendar.getInstance()

        if (calendar[Calendar.MONTH] == tCalendar[Calendar.MONTH]){
            return true
        }
        return false

    }

    private fun addProfileExperience() {
        binding.apply {

            val startingDate = dateFromText.text
            val endingDate = dateToText.text
            val jobTitle = experienceInputText.text

            if (startingDate.toString().trim().isEmpty() || endingDate.toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Date can't be empty", Toast.LENGTH_SHORT).show()
                return
            }
            if (jobTitle.toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Job title can't be empty", Toast.LENGTH_SHORT).show()
                return
            }

            val date = if (checkIfDateIsPresent(endDate?:0L)) "Present" else getShortDate(endDate?:0L)

            val experience = Experience(
                jobTitle = experienceInputText.text.toString(),
                company = experienceCompanyText.text.toString(),
                date = getShortDate(startDate ?: 0L) + " - " + date
            )
            addExperience(experience)
            experienceInputText.text?.clear()
            experienceCompanyText.text?.clear()
            dateFromText.text?.clear()
            dateToText.text?.clear()
        }
    }


}