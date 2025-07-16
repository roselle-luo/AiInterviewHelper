package com.example.interviewhelper.ui.component

import android.Manifest
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.interviewhelper.viewmodel.InterviewController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionHandler(
    viewModel: InterviewController = viewModel(),
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    // 启动权限申请
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when {
        cameraPermissionState.status.isGranted -> {
            // 权限已授予，启动相机
            LaunchedEffect(Unit) {
                viewModel.startCamera(lifecycleOwner, context)
                onPermissionGranted()
            }
        }

        cameraPermissionState.status.shouldShowRationale -> {
            // 显示说明
            Toast.makeText(context, "需要相机权限以进行视频面试", Toast.LENGTH_SHORT).show()
        }

        else -> {
            // 拒绝或永久拒绝
            Toast.makeText(context, "相机权限被拒绝，无法使用视频功能", Toast.LENGTH_SHORT).show()
        }
    }
}