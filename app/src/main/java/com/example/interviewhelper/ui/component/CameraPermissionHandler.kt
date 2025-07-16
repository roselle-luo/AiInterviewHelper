package com.example.interviewhelper.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.interviewhelper.viewmodel.InterviewController
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionHandler(
    viewModel: InterviewController,
    onGranted: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (cameraPermissionState.status.isGranted) {
            // 已授权，启动摄像头
            viewModel.startCamera(lifecycleOwner, context)
            onGranted()
        } else {
            // 请求权限
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // 可选 UI 提示（如果权限被拒绝）
    if (cameraPermissionState.status.shouldShowRationale) {
        Text("需要相机权限才能进行视频通话")
    }
}
