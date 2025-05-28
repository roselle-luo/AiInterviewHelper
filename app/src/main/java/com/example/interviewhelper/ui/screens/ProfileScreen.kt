package com.example.interviewhelper.ui.screens // 根据你的包名修改

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.theme.Peach40
import com.example.interviewhelper.viewmodel.ProfileViewModel


data class MenuListItem(val icon: ImageVector, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.userInfo.collectAsState()

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
                        .padding(horizontal = 20.dp, vertical = 16.dp), // 调整整体内边距
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
                                color = Peach40,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp), // 卡片本身的圆角
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // 使卡片透明以显示渐变
                    ) {
                        // 顶部用户信息栏
                        Column (
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
                                    painter = painterResource(id = R.drawable.ic_launcher_background), // 替换为你的头像资源
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

                        // 你原来的邮箱和学校信息（如果想放在卡片内）
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

                    // 三个主要功能入口
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp), // 微调内边距，使其与卡片对齐
                        horizontalArrangement = Arrangement.SpaceAround // 调整排列方式
                    ) {
                        FeatureButton(
                            icon = Icons.Filled.Person,
                            text = "科协成员",
                            onClick = { /* TODO */ })
                        Spacer(modifier = Modifier.width(8.dp)) // 增加间距
                        FeatureButton(
                            icon = Icons.Filled.Home,
                            text = "学校官网",
                            onClick = { /* TODO */ })
                        Spacer(modifier = Modifier.width(8.dp)) // 增加间距
                        FeatureButton(
                            icon = Icons.Filled.Place,
                            text = "学校地图",
                            onClick = { /* TODO */ })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 切换模式按钮
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp), // 微调内边距
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: 处理夜间模式切换 */ },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp), // 确保高度一致
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Favorite, contentDescription = "夜间模式")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("夜间模式")
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { /* TODO: 处理主题更换 */ },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp) // 确保高度一致
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Create, contentDescription = "主题更换")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("主题更换")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 列表样式的菜单项
                    MenuList(
                        items = listOf(
                            MenuListItem(Icons.Filled.Add, "签到记录"),
                            MenuListItem(Icons.Filled.Add, "发帖记录"),
                            MenuListItem(Icons.Filled.Add, "借阅记录"),
                            MenuListItem(Icons.Filled.Share, "分享应用"),
                            MenuListItem(Icons.Filled.Build, "便捷工具"),
                            MenuListItem(Icons.Filled.Add, "关于应用")
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
            .padding(vertical = 4.dp), // 调整垂直间距
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
            color = textColor.copy(alpha = 0.9f) // 稍微降低透明度
        )
    }
}


@Composable
fun FeatureButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(80.dp),
        shape = RoundedCornerShape(12.dp), // 稍大的圆角
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh) // 浅灰色背景
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
fun MenuList(items: List<MenuListItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp) // 与其他元素对齐
            .clip(RoundedCornerShape(12.dp)) // 整个列表区域的圆角
            .background(MaterialTheme.colorScheme.surfaceContainerHigh) // 列表背景色
    ) {
        items.forEach { item ->
            MenuItem(item = item)
            // 除了最后一个，都添加 Divider
            if (items.indexOf(item) < items.size - 1) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun MenuItem(item: MenuListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {})
            .padding(vertical = 12.dp, horizontal = 16.dp), // 调整内边距
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                item.icon,
                contentDescription = item.text,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(item.text, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            Icons.Filled.Person,
            contentDescription = "查看更多",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

//@Preview(showBackground = true, widthDp = 360, heightDp = 800) // 调整预览大小以更好地查看全貌
//@Composable
//fun ProfileScreenPreview() {
//    // 这是一个模拟的 ViewModel 和 NavController，仅用于预览
//    class MockProfileViewModel : ProfileViewModel() {
//        private val _mockUserInfo = MutableStateFlow(
//            User(
//                name = "张浩",
//                department = "软件学部",
//                id = "2300320225",
//                email = "zhanghao@example.com",
//                school = "你的大学名称"
//            )
//        )
//        override val userInfo: StateFlow<User> = _mockUserInfo
//    }
//
//    com.example.yourprojectname.ui.theme.YourAppNameTheme { // 替换为你的主题
//        ProfileScreen(navController = rememberNavController(), viewModel = MockProfileViewModel())
//    }
//}