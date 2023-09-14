package com.tutorials.hngx2

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
                profileImg = "",
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
}