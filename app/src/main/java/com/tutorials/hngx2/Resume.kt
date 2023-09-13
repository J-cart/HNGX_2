package com.tutorials.hngx2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tutorials.hngx2.databinding.FragmentResumeBinding


class Resume : Fragment() {
    private lateinit var binding: FragmentResumeBinding
    private val skillsAdapter by lazy { SkillsAdapter(false) }
    private val expAdapter by lazy { ExperienceAdapter(false) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            experienceRv.adapter = expAdapter
            skillsRv.adapter = skillsAdapter
            skillsAdapter.submitList(DataBank.profile.skills)
            expAdapter.submitList(DataBank.profile.experience)
            jobTitleText.text = DataBank.profile.jobTitle
            fullNameText.text = DataBank.profile.fullName
            locationText.text = DataBank.profile.location
            summaryText.text = DataBank.profile.summary
        }
    }

}