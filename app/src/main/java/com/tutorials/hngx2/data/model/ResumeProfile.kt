package com.tutorials.hngx2.data.model

data class ResumeProfile(
    val profileImg: String = "",
    val fullName: String = "",
    val jobTitle: String = "",
    val location: String = "",
    val summary: String = "",
    val skills: List<String> = emptyList(),
    val experience: List<Experience> = emptyList(),
)