package com.example.interviewhelper.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object LoadingDialogController {
    var isShowing by mutableStateOf(false)
    var message by mutableStateOf("加载中...")

    fun show(msg: String = "加载中...") {
        message = msg
        isShowing = true
    }

    fun hide() {
        isShowing = false
    }
}