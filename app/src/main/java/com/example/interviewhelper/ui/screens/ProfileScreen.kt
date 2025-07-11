package com.example.interviewhelper.ui.screens // 根据你的包名修改

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.component.RowButtonWithIcon
import com.example.interviewhelper.ui.theme.LightBlue40
import com.example.interviewhelper.ui.theme.Peach40
import com.example.interviewhelper.ui.theme.Peach50
import com.example.interviewhelper.ui.theme.Peach60
import com.example.interviewhelper.ui.theme.Peach80
import com.example.interviewhelper.ui.theme.Rose80
import com.example.interviewhelper.ui.theme.Sun50
import com.example.interviewhelper.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay


data class MenuListItem(
    val icon: ImageVector,
    val text: String,
    val color: Color = Color.Unspecified
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {

    val user by viewModel.userInfo.collectAsState()
    val context = LocalContext.current
    val images = viewModel.imagesList
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { images.size }

    // 自动轮播（每 3 秒换一张）
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 允许页面滚动
        ) {
            AnimatedVisibility(
                visible = true, // 初始可见
                enter = fadeIn(animationSpec = tween(durationMillis = 800)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(durationMillis = 600)
                ),
                // exit 动画在移除时使用，这里是静态页面，暂时用不到
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // 填充宽度
                        .padding(horizontal = 20.dp)
                        .padding(top = 6.dp), // 调整整体内边距
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 主页卡片 - 用户信息部分
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // 高度随内容自适应
                            // 将背景渐变应用到 Card 的 Modifier 上
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Peach40, Peach50,
                                        Peach60, Peach80
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp), // 卡片本身的圆角
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // 使卡片透明以显示渐变
                    ) {
                        // 顶部用户信息栏
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // 占位符头像 - 你需要确保 R.drawable.ic_profile_placeholder 存在
                                Image(
                                    painter = painterResource(id = R.drawable.common_avatar), // 替换为你的头像资源
                                    contentDescription = "用户头像",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(
                                            2.dp,
                                            Color.White.copy(alpha = 0.7f),
                                            CircleShape
                                        ) // 白色边框
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        user.email ?: "未知用户",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "${user.gender}性",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            "${user.age}岁",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 22.dp)
                                .padding(bottom = 12.dp)
                        ) {
                            InfoRow(
                                label = "学校",
                                value = user.school ?: "N/A",
                                textColor = Color.White
                            )
                            InfoRow(
                                label = "专业",
                                value = user.subject ?: "N/A",
                                textColor = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RowButtonWithIcon(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            {},
                            R.drawable.preference,
                            "面试偏好",
                            backGroundColor = Peach50,
                        )
                        Spacer(Modifier.width(12.dp))
                        RowButtonWithIcon(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            { Toast.makeText(context, "简历管理", Toast.LENGTH_SHORT).show() },
                            R.drawable.resume,
                            "简历管理",
                            textColor = Color.Black,
                            MaterialTheme.colorScheme.background,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) { page ->
                        Image(
                            painter = painterResource(id = images[page]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF2D55), // 亮玫红（鲜艳）
                                        Color(0xFFFF4A8D), // 粉红
                                        Color(0xFFFFA3B1)  // 浅粉色（亮）
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)  // 背景圆角必须和按钮形状匹配
                            ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, color = Rose80),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row {
                            Text(
                                "点击接收你的综合分析报告",
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            repeat(3) {
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "点击获取你的专属面经推荐",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 列表样式的菜单项
                    MenuList(
                        items = listOf(
                            MenuListItem(
                                ImageVector.vectorResource(R.drawable.history),
                                "模拟面试记录",
                                color = Peach40
                            ),
                            MenuListItem(
                                Icons.Default.Star,
                                "面经收藏",
                                color = Sun50
                            ),
                            MenuListItem(Icons.Filled.Share, "分享应用", color = LightBlue40),
                            MenuListItem(Icons.Filled.Settings, "设置"),
                            MenuListItem(Icons.Filled.Info, "关于应用", color = Peach60)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp)) // 底部留一些空间
                }
            }
        }
    }
}

// 封装的 InfoRow，可以接收不同的文本颜色
@Composable
fun InfoRow(label: String, value: String, textColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp), // 调整垂直间距
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = textColor,
            modifier = Modifier.width(60.dp) // 调整标签宽度
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor.copy(alpha = 0.95f) // 稍微降低透明度
        )
    }
}

@Composable
fun MenuList(items: List<MenuListItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        items.forEach { item ->
            MenuItem(item = item)
            // 除了最后一个，都添加 Divider
//            if (items.indexOf(item) < items.size - 1) {
//                Divider(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    color = MaterialTheme.colorScheme.outlineVariant
//                )
//            }
        }
    }
}

@Composable
fun MenuItem(item: MenuListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {})
            .padding(vertical = 16.dp), // 调整内边距
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                item.icon,
                contentDescription = item.text,
                Modifier.size(26.dp),
                tint = item.color,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(item.text, fontSize = 18.sp)
        }
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = "查看详情",
        )
    }
}