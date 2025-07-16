package com.example.interviewhelper.ui.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.R
import com.example.interviewhelper.data.model.ChatMessage
import com.example.interviewhelper.data.model.MessageRole
import com.example.interviewhelper.ui.theme.Aqua40
import com.example.interviewhelper.ui.theme.Aqua80
import com.example.interviewhelper.ui.theme.MintBackground
import com.example.interviewhelper.ui.theme.Peach50
import com.example.interviewhelper.viewmodel.ChatScreenViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ChatScreen(navController: NavController, viewModel: ChatScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        modifier = Modifier.pointerInput(Unit) { detectTapGestures(onTap = { focus.clearFocus() }) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("AI问答", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Row {
                    IconButton(onClick = { viewModel.clearMessages() }) {
                        Box(
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.add),
                                contentDescription = "添加新会话"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                    IconButton(onClick = {
                        viewModel.soundSwitch.value = !viewModel.soundSwitch.value
                    }) {
                        Box(
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                when {
                                    viewModel.soundSwitch.value -> ImageVector.vectorResource(R.drawable.sound_open)
                                    else -> ImageVector.vectorResource(R.drawable.sound_close)
                                },
                                contentDescription = "声音播报开关"
                            )
                        }
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 消息列表
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 8.dp, top = 8.dp)
                ) {
                    items(uiState.messages) { message -> // 反转列表，因为 reverseLayout 会从底部向上填充
                        ChatMessageItem(message = message, screenWidth = screenWidth)
                    }
                }

                // 加载指示器
                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                // 输入框和发送按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("输入你的问题...") },
                        maxLines = 5,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = "" // 发送后清空输入框
                                focus.clearFocus()
                            } else Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show()
                        },
                        containerColor = Aqua40,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "发送")
                    }
                }
            }
        }
    )
}

@Composable
fun ChatMessageItem(message: ChatMessage, screenWidth: Dp) {
    val isUser = message.role == MessageRole.USER
    if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor =
        if (isUser) MaterialTheme.colorScheme.primary else Aqua80

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            modifier = Modifier.widthIn(max = screenWidth * 0.7f) // 限制气泡最大宽度
        ) {
            MarkdownText(
                markdown = message.content,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
