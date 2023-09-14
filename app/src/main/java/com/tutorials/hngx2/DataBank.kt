package com.tutorials.hngx2

import com.tutorials.hngx2.model.Experience
import com.tutorials.hngx2.model.ResumeProfile
import kotlinx.coroutines.flow.MutableStateFlow

object DataBank {

    private val skills = listOf(
        "Team player",
        "Hard working",
        "Committed",
        "Critical thinking",
        "Problem solving",
        "Accountable",
        "Fast learner",
    )

   private val experience = listOf(
        Experience(
            jobTitle = "Android Developer",
            company = "Facebook",
            date = "Jan 23  till present"
        ),
        Experience(
            jobTitle = "Android Engineer",
            company = "Apple",
            date = "Mar 23  till present"
        ),
        Experience(
            jobTitle = "Mobile Developer",
            company = "Apple",
            date = "Jan 22  till present"
        ),
        Experience(
            jobTitle = "Android Developer",
            company = "Netflix",
            date = "Feb 21  till present"
        ),
        Experience(
            jobTitle = "Android Engineer",
            company = "Google",
            date = "Dec 23  till present"
        ),
    )

    val profile = ResumeProfile(
        fullName = "John Doe",
        jobTitle = "Android Engineer",
        location = "Mars, Milky way",
        summary = "Independent, self- motivated and personable android developer eager to" +
                " join as an intern to help and apply my knowledge and skills to a real-world setting and make" +
                " a meaningful contribution to the world of technology.",
        skills = skills,
        experience = experience
    )
}