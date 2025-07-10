package com.example.interviewhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.example.interviewhelper.R
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.data.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {
    val userInfo: MutableStateFlow<UserInfo> = globalData.userInfo
    val imagesList: List<Int> =
        listOf(R.drawable.alibaba, R.drawable.bytedance, R.drawable.bilibili, R.drawable.tencent)
}