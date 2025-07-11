package com.example.interviewhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.example.interviewhelper.common.GlobalData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewSettingViewModel @Inject constructor(
    globalData: GlobalData
) : ViewModel() {
}