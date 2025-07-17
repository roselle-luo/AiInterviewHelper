package com.example.interviewhelper.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.component.CameraPermissionHandler
import com.example.interviewhelper.ui.component.CircleIconButton
import com.example.interviewhelper.ui.theme.MintBackground
import com.example.interviewhelper.viewmodel.InterviewController

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun InterviewScreen(
    navController: NavController, viewModel: InterviewController = hiltViewModel()
) {

    val context = LocalContext.current
    val micSwitch by viewModel.micSwitch
    val videoSwitch by viewModel.videoSwitch

    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView by viewModel.previewView.observeAsState()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.talk))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = viewModel.isTalking.value,
        iterations = LottieConstants.IterateForever
    )
    val endProgress = if (!viewModel.isTalking.value) 1f else progress

    val listState = rememberLazyListState()


    CameraPermissionHandler(viewModel = viewModel) {
        // 权限授予后额外操作（可选）
        Log.d("Permission", "Camera permission granted!")
    }

    LaunchedEffect(Unit) {
        viewModel.startCamera(lifecycleOwner, context)
        if (viewModel.questions.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.questions.lastIndex)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MintBackground)) {

        // 动画模拟对话
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),// 确保Column居顶（也可以不写，默认）
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 动画模拟对话
            LottieAnimation(
                composition,
                endProgress,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // ✅ 加字幕
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 36.dp),
                state = listState,
                contentPadding = PaddingValues(6.dp)
            ) {
                items(viewModel.questions.size) { index ->
                    Text(
                        text = "问题${index}：${viewModel.questions[index]}",
                        color = Color.Black.copy(alpha = 0.5f),
                        fontSize = 18.sp,
                        maxLines = 7,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }


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
