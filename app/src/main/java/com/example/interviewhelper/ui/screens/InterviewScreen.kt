package com.example.interviewhelper.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.component.CameraPermissionHandler
import com.example.interviewhelper.ui.component.CircleIconButton
import com.example.interviewhelper.viewmodel.InterviewController

@Composable
fun InterviewScreen(
    navController: NavController, viewModel: InterviewController = hiltViewModel()
) {

    val context = LocalContext.current
    val micSwitch by viewModel.micSwitch
    val videoSwitch by viewModel.videoSwitch
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView by viewModel.previewView.observeAsState()

    CameraPermissionHandler(viewModel = viewModel) {
        // 权限授予后额外操作（可选）
        Log.d("Permission", "Camera permission granted!")
    }

    LaunchedEffect(Unit) {
        viewModel.startCamera(lifecycleOwner, context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 远端视频（全屏）
        AndroidView(
            factory = { context ->
                SurfaceView(context).apply {
                    // 设置远端视频流
                    // renderer.setRemoteVideo(this)
                }
            }, modifier = Modifier.fillMaxSize()
        )

        previewView?.let { pv ->
            // 本地视频（右上角小窗口）
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 56.dp, end = 24.dp)
                    .size(120.dp, 180.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)) // 白色背景带圆角
                    .clip(RoundedCornerShape(8.dp)) // 裁剪圆角
            ) {
                AndroidView(
                    factory = { pv },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


        // 底部控制栏
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 72.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center  // 让内容居中
            ) {
                CircleIconButton(
                    iconResId = if (micSwitch) R.drawable.micro_open else R.drawable.micro_close,
                    iconTint = Color.Black,
                    onClick = { viewModel.micSwitch.value = !viewModel.micSwitch.value },
                )
            }
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center  // 让内容居中
            ) {
                CircleIconButton(
                    iconResId = R.drawable.close_phone,
                    iconTint = Color.Red,
                    onClick = {
                        navController.popBackStack()
                        Toast.makeText(context, "挂断", Toast.LENGTH_SHORT).show()
                    },
                )
            }
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center  // 让内容居中
            ) {
                CircleIconButton(
                    iconResId = if (videoSwitch) R.drawable.video_open else R.drawable.video_close,
                    iconTint = Color.Black,
                    onClick = {
                        viewModel.videoSwitch.value = !viewModel.videoSwitch.value
                    },
                )
            }
        }
    }
}
