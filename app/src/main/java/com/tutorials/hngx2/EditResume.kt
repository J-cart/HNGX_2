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
import com.tutorials.hngx2.arch.ResumeViewModel
import com.tutorials.hngx2.databinding.FragmentEditResumeBinding
import com.tutorials.hngx2.model.Experience
import com.tutorials.hngx2.model.ResumeProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EditResume : Fragment() {
    private lateinit var binding: FragmentEditResumeBinding
    private val skillsAdapter by lazy { SkillsAdapter(true) }
    private val expAdapter by lazy { ExperienceAdapter(true) }
    private val viewModel by activityViewModels<ResumeViewModel>()
    private var imageUri:String? = null


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
                val experience = Experience(
                    jobTitle = experienceInputText.text.toString(),
                    company = experienceCompanyText.text.toString(),
                    date = dateFromText.text.toString() + " " + dateToText.text
                )
                addExperience(experience)
                experienceInputText.text?.clear()
                experienceCompanyText.text?.clear()
                dateFromText.text?.clear()
                dateToText.text?.clear()
            }

            editImgBtn.setOnClickListener {
                imgSelector()
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
}