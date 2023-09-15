package com.tutorials.hngx2.ui.arch

import androidx.lifecycle.ViewModel
import com.tutorials.hngx2.data.model.Experience
import com.tutorials.hngx2.data.model.ResumeProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ResumeViewModel : ViewModel() {

    var profileFlow = MutableStateFlow<ResumeProfile>(ResumeProfile())


   /* init {
        profileFlow.value = DataBank.profile
    }*/

    fun addExperience(experience: Experience) {
        val currentList = profileFlow.value.experience.toMutableList()
        currentList.add(experience)
        profileFlow.update {
            it.copy(experience = currentList.toList())
        }
    }

    fun removeExperience(experience: Experience) {
        val currentList = profileFlow.value.experience.toMutableList()
        currentList.remove(experience)
        profileFlow.update {
            it.copy(experience = currentList.toList())
        }
    }
    fun addSkill(skill: String) {
        val currentList = profileFlow.value.skills.toMutableList()
        currentList.add(skill)
        profileFlow.update {
            it.copy(skills = currentList.toList())
        }
    }

    fun removeSkill(skill: String) {
        val currentList = profileFlow.value.skills.toMutableList()
        currentList.remove(skill)
        profileFlow.update {
            it.copy(skills = currentList.toList())
        }
    }

    fun updateProfile(resumeProfile: ResumeProfile){
        profileFlow.update { resumeProfile }
    }

}