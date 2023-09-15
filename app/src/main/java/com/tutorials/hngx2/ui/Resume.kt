package com.tutorials.hngx2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.tutorials.hngx2.ui.adapter.ExperienceAdapter
import com.tutorials.hngx2.R
import com.tutorials.hngx2.ui.adapter.SkillsAdapter
import com.tutorials.hngx2.ui.arch.ResumeViewModel
import com.tutorials.hngx2.databinding.FragmentResumeBinding
import kotlinx.coroutines.launch


class Resume : Fragment() {
    private lateinit var binding: FragmentResumeBinding
    private val skillsAdapter by lazy { SkillsAdapter(false) }
    private val expAdapter by lazy { ExperienceAdapter(false) }
    private val viewModel by activityViewModels<ResumeViewModel>()

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

            lifecycleScope.launch {
                viewModel.profileFlow.collect{
                    binding.profileImg.load(it.profileImg){
                        placeholder(R.drawable.ic_launcher_foreground)
                        error(R.drawable.ic_launcher_foreground)
                    }
                    emptyExpText.isVisible = it.experience.isEmpty()
                    emptySkillsText.isVisible = it.skills.isEmpty()
                    skillsAdapter.submitList(it.skills)
                    expAdapter.submitList(it.experience)
                    jobTitleText.text = it.jobTitle.ifEmpty { resources.getString(R.string.job_title) }
                    fullNameText.text = it.fullName.ifEmpty { resources.getString(R.string.profile_name) }
                    locationText.text = it.location.ifEmpty { resources.getString(R.string.location) }
                    summaryText.text = it.summary.ifEmpty { resources.getString(R.string.profile_summary) }
                }
            }


            editBtn.setOnClickListener {
                val route = ResumeDirections.actionResumeToEditResume()
                findNavController().navigate(route)
            }
        }
    }

}