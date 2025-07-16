package com.example.interviewhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewhelper.data.model.Education
import com.example.interviewhelper.data.model.PersonalInfo
import com.example.interviewhelper.data.model.Project
import com.example.interviewhelper.data.model.ResumeData
import com.example.interviewhelper.data.model.WorkExperience
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


@HiltViewModel
class ResumeViewModel @Inject constructor() : ViewModel() {

    private val _resumeData = MutableStateFlow(ResumeData())
    val resumeData: StateFlow<ResumeData> = _resumeData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 更新个人信息
    fun updatePersonalInfo(personalInfo: PersonalInfo) {
        _resumeData.value = _resumeData.value.copy(personalInfo = personalInfo)
    }

    // 添加工作经历
    fun addWorkExperience(workExperience: WorkExperience) {
        val newExperience = workExperience.copy(id = UUID.randomUUID().toString())
        val currentList = _resumeData.value.workExperiences.toMutableList()
        currentList.add(newExperience)
        _resumeData.value = _resumeData.value.copy(workExperiences = currentList)
    }

    // 更新工作经历
    fun updateWorkExperience(workExperience: WorkExperience) {
        val currentList = _resumeData.value.workExperiences.toMutableList()
        val index = currentList.indexOfFirst { it.id == workExperience.id }
        if (index != -1) {
            currentList[index] = workExperience
            _resumeData.value = _resumeData.value.copy(workExperiences = currentList)
        }
    }

    // 删除工作经历
    fun removeWorkExperience(id: String) {
        val currentList = _resumeData.value.workExperiences.toMutableList()
        currentList.removeAll { it.id == id }
        _resumeData.value = _resumeData.value.copy(workExperiences = currentList)
    }

    // 添加教育经历
    fun addEducation(education: Education) {
        val newEducation = education.copy(id = UUID.randomUUID().toString())
        val currentList = _resumeData.value.educations.toMutableList()
        currentList.add(newEducation)
        _resumeData.value = _resumeData.value.copy(educations = currentList)
    }

    // 更新教育经历
    fun updateEducation(education: Education) {
        val currentList = _resumeData.value.educations.toMutableList()
        val index = currentList.indexOfFirst { it.id == education.id }
        if (index != -1) {
            currentList[index] = education
            _resumeData.value = _resumeData.value.copy(educations = currentList)
        }
    }

    // 删除教育经历
    fun removeEducation(id: String) {
        val currentList = _resumeData.value.educations.toMutableList()
        currentList.removeAll { it.id == id }
        _resumeData.value = _resumeData.value.copy(educations = currentList)
    }

    // 添加技能
    fun addSkill(skill: String) {
        if (skill.isNotBlank()) {
            val currentList = _resumeData.value.skills.toMutableList()
            currentList.add(skill)
            _resumeData.value = _resumeData.value.copy(skills = currentList)
        }
    }

    // 删除技能
    fun removeSkill(skill: String) {
        val currentList = _resumeData.value.skills.toMutableList()
        currentList.remove(skill)
        _resumeData.value = _resumeData.value.copy(skills = currentList)
    }

    // 添加项目
    fun addProject(project: Project) {
        val newProject = project.copy(id = UUID.randomUUID().toString())
        val currentList = _resumeData.value.projects.toMutableList()
        currentList.add(newProject)
        _resumeData.value = _resumeData.value.copy(projects = currentList)
    }

    // 更新项目
    fun updateProject(project: Project) {
        val currentList = _resumeData.value.projects.toMutableList()
        val index = currentList.indexOfFirst { it.id == project.id }
        if (index != -1) {
            currentList[index] = project
            _resumeData.value = _resumeData.value.copy(projects = currentList)
        }
    }

    // 删除项目
    fun removeProject(id: String) {
        val currentList = _resumeData.value.projects.toMutableList()
        currentList.removeAll { it.id == id }
        _resumeData.value = _resumeData.value.copy(projects = currentList)
    }

    // 保存简历
    fun saveResume() {
        viewModelScope.launch {
            _isLoading.value = true
            // 这里可以添加保存到数据库或API的逻辑
            kotlinx.coroutines.delay(1000) // 模拟网络请求
            _isLoading.value = false
        }
    }
}