@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.theme.DeeperPurple
import com.example.interviewhelper.ui.theme.LighterPurple
import com.example.interviewhelper.ui.theme.Peach40
import com.example.interviewhelper.viewmodel.InterviewScreenViewModel

@Composable
fun Interview(navController: NavController, viewModel: InterviewScreenViewModel = hiltViewModel()) {

    val gradientTitleColors = listOf(
        Color(0xFF1E90FF), // Dodger Blue (道奇蓝)
        Color(0xFF4169E1), // Royal Blue (宝蓝色)
        Color(0xFF8F00FF), // Electric Purple (电光紫)
        Color(0xFF8A2BE2),  // 回到蓝紫色
        Color(0xFF6A11AB), // Indigo (靛蓝色)
    )

    val gradientButtonColors = listOf(
        DeeperPurple,
        LighterPurple
    )

    val buttonBrush = Brush.horizontalGradient(gradientButtonColors)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        //Color(0xFFBEA8FA), // Royal Blue (宝蓝色)
                        Color(0xFFC9B8F8), // Royal Blue (宝蓝色)
                        Color(0xFF2355F1), // Electric Purple (电光紫)
                    )
                )
            )
    ) {

        BottomSheet(
            viewModel.bottomSheetSwitch,
            viewModel.interviewSubject,
            viewModel.selectedSubject
        )

        Image(
            painter = painterResource(id = R.drawable.interview_background),
            contentDescription = "Background Image", // 提供无障碍描述
            modifier = Modifier.fillMaxSize(), // 让图片铺满整个Box
            contentScale = ContentScale.Fit // 裁剪图片以填充边界，可能会裁掉部分内容
        )

    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "AI模拟面试训练",
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            fontSize = 28.sp,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientTitleColors
                )
            )
        )

        Column {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("选择你的面试岗位:", color = Color.White, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    onClick = { viewModel.bottomSheetSwitch.value = true },
                    color = Color.Transparent
                ) {
                    Row {
                        Text(
                            viewModel.selectedSubject.value,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            Icons.Filled.Edit,
                            tint = Peach40,
                            contentDescription = "选择职位"
                        )
                        Text(
                            "修改",
                            color = Peach40,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp)
                        .padding(start = 16.dp),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // 按钮本身的颜色设置为透明
                        contentColor = DeeperPurple// 文本颜色
                    ),
                    border = BorderStroke(2.dp, DeeperPurple),
                    shape = MaterialTheme.shapes.medium, // 形状要和背景的形状一致
                    content = {
                        Text(
                            text = "定制面试间",
                            fontSize = 16.sp,
                            letterSpacing = 1.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp)
                        .background(buttonBrush, shape = MaterialTheme.shapes.medium),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // 按钮本身的颜色设置为透明
                        contentColor = Color.White // 文本颜色
                    ),
                    shape = MaterialTheme.shapes.medium, // 形状要和背景的形状一致
                    content = {
                        Text(
                            text = "开始面试",
                            fontSize = 16.sp,
                            letterSpacing = 6.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                )
            }
        }

    }
}


@Composable
fun BottomSheet(
    showBottomSheet: MutableState<Boolean>,
    selectOptions: List<String>,
    selectedJobs: MutableState<String>
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            Column {
                Text(
                    "选择岗位",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    fontSize = 22.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    selectOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == selectedJobs.value),
                                    onClick = {
                                        selectedJobs.value = text
                                        showBottomSheet.value = false
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedJobs.value),
                                onClick = {
                                    selectedJobs.value = text
                                    showBottomSheet.value = false
                                }
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
