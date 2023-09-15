package com.tutorials.hngx2.ui.arch

import androidx.lifecycle.ViewModel
import com.tutorials.hngx2.data.model.Experience
import com.tutorials.hngx2.data.model.ResumeProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ResumeViewModel : ViewModel() {

    var profileFlow = MutableStateFlow<ResumeProfile>(ResumeProfile())
    var skillsFlow = MutableStateFlow<List<String>>(emptyList())
    var experienceFlow = MutableStateFlow<List<Experience>>(emptyList())


   /* init {
        profileFlow.value = DataBank.profile
    }*/

    fun addExperience(experience: Experience) {
        val currentList = experienceFlow.value.toMutableList()
        currentList.add(experience)
        experienceFlow.update {
           currentList.toList()
        }
    }

    fun removeExperience(experience: Experience) {
        val currentList = experienceFlow.value.toMutableList()
        currentList.remove(experience)
        experienceFlow.update {
           currentList.toList()
        }
    }
    fun addSkill(skill: String) {
        val currentList = skillsFlow.value.toMutableList()
        currentList.add(skill)
        skillsFlow.update {
            currentList.toList()
        }
    }

    fun removeSkill(skill: String) {
        val currentList = skillsFlow.value.toMutableList()
        currentList.remove(skill)
        skillsFlow.update {
           currentList.toList()
        }
    }

    fun updateProfile(resumeProfile: ResumeProfile){
        profileFlow.update { resumeProfile }
    }

}