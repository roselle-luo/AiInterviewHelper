package com.example.interviewhelper.data.model

data class ResumeData(
    val personalInfo: PersonalInfo = PersonalInfo(),
    val workExperiences: List<WorkExperience> = emptyList(),
    val educations: List<Education> = emptyList(),
    val skills: List<String> = emptyList(),
    val projects: List<Project> = emptyList()
)

data class PersonalInfo(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val summary: String = ""
)

data class WorkExperience(
    val id: String = "",
    val company: String = "",
    val position: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val isCurrentJob: Boolean = false
)

data class Education(
    val id: String = "",
    val school: String = "",
    val degree: String = "",
    val major: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val gpa: String = ""
)

data class Project(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val technologies: String = "",
    val url: String = ""
)
