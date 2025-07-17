package com.example.interviewhelper.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.interviewhelper.viewmodel.InterviewController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionHandler(
    viewModel: InterviewController,
    onGranted: () -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (permissionsState.allPermissionsGranted) {
            // 所有权限已授权，启动摄像头和音频采集
            viewModel.startCamera(lifecycleOwner, context)
            viewModel.startAudioCapture()
            onGranted()
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    if (permissionsState.shouldShowRationale) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("应用需要相机和麦克风权限以启用视频通话功能。")
        }
    }
}
