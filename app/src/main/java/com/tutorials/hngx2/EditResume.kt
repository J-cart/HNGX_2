package com.tutorials.hngx2

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutorials.hngx2.databinding.FragmentEditResumeBinding
import com.tutorials.hngx2.model.Experience
import com.tutorials.hngx2.model.ResumeProfile


class EditResume : Fragment() {
    private lateinit var binding: FragmentEditResumeBinding
    private val skillsAdapter by lazy { SkillsAdapter(true) }
    private val expAdapter by lazy { ExperienceAdapter(true) }


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


            skillsAdapter.submitList(DataBank.profile.skills)
            expAdapter.submitList(DataBank.profile.experience)

            fullNameText.setText(DataBank.profile.fullName)
            jobTitleText.setText(DataBank.profile.jobTitle)
            locationText.setText(DataBank.profile.location)
            profileSummaryText.setText(DataBank.profile.summary)

            expAdapter.adapterDeleteClickListener {
//                DataBank.profile.experience.remove(it)
            }
            skillsAdapter.adapterDeleteClickListener {
//                DataBank.profile.skills.remove(it)
            }

//            addSkillBtn.setOnClickListener {
//                addSkill(skillInputText.text.toString())
//            }
//            addExpBtn.setOnClickListener {
//                val experience = Experience(
//                    jobTitle = experienceInputText.text.toString(),
//                    company = experienceCompanyText.text.toString(),
//                    date = dateFromText.text.toString() + " " + dateToText.text
//                )
//                addExperience(experience)
//            }


        }

    }

    private fun saveProfile() {

        binding.apply {
            ResumeProfile(
                profileImg = "",
                fullName = fullNameText.text.toString().trim(),
                jobTitle = jobTitleText.text.toString().trim(),
                location = locationText.text.toString().trim(),
                summary = profileSummaryText.text.toString().trim(),
                skills = arrayListOf(),
                experience = arrayListOf()
            )
        }

    }

    private fun addSkill(skill: String) {
        if (skill.trim().isNotEmpty()) {
//            DataBank.profile.skills.add(skill)
            return
        }
        Toast.makeText(requireContext(), "Skill input can't be empty", Toast.LENGTH_SHORT).show()
    }

    private fun addExperience(experience: Experience) {
        if (experience.jobTitle.trim().isNotEmpty()) {
//            DataBank.profile.experience.add(experience)
            return
        }
        Toast.makeText(requireContext(), "Skill input can't be empty", Toast.LENGTH_SHORT).show()

    }
}