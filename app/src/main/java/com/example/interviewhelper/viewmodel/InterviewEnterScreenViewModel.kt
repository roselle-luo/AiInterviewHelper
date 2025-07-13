package com.example.interviewhelper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.interviewhelper.common.GlobalData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewEnterScreenViewModel @Inject constructor(
    globalData: GlobalData
) : ViewModel() {

    val interviewSubject = listOf(
        "前端开发",
        "人工智能",
        "大数据",
        "物联网",
        "后端开发",
        "移动开发",
    )

    val bottomSheetSwitch = mutableStateOf(false)
    val selectedSubject = mutableStateOf("前端开发")

}