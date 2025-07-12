package com.example.interviewhelper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.data.model.ChatMessage
import com.example.interviewhelper.data.model.MessageRole
import com.example.interviewhelper.data.repository.SparkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


// UI状态数据类
data class AiChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val globalData: GlobalData,
    private val sparkRepository: SparkRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(AiChatUiState()) // 使用AiChatUiState的默认值初始化
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()
    val response = MutableStateFlow("")
    val soundSwitch = mutableStateOf(false)

    // 假设的 AI 回复列表
    private val aiResponses = listOf(
        "您好，我是AI助手，很高兴为您服务。",
        "Compose 是现代 Android UI 工具包，它简化了UI开发。",
        "MVVM 架构模式有助于分离关注点，使代码更易于测试和维护。",
        "Retrofit 和 OkHttp 是 Android 网络请求的常用库。",
        "FastAPI 是一个高性能的 Python web 框架。",
        "Redis 通常用于缓存和实时数据存储。",
        "MariaDB 是一个流行的关系型数据库。",
        "音视频连接处理通常涉及到 WebRTC 或自定义的流媒体协议。",
        "如果您有具体的技术问题，我可以尝试为您解答。",
        "请问您想了解哪些关于 Jetpack Compose 的内容？"
    )

    init {
        // 你可以在这里进行任何你想要的初始设置。
        // 例如，添加一条初始欢迎消息：
        _uiState.update { currentState ->
            currentState.copy(
                messages = listOf(
                    ChatMessage(
                        content = "您好！我是面试助手，有什么可以帮助您的？",
                        role = MessageRole.AI
                    )
                ),
                isLoading = false // 初始不加载
            )
        }
        // 如果 GlobalData 里有需要读取的初始设置，也可以在这里读取

    }

    /**
     * 发送用户消息并模拟 AI 回复
     */
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(content = text, role = MessageRole.USER)

        // 创建空 AI 消息（可通过 id 找到并更新它的 content）
        val aiMessageId = UUID.randomUUID().toString()
        val aiMessage = ChatMessage(id = aiMessageId, content = "", role = MessageRole.AI)

        // 添加 USER + 空 AI 消息
        _uiState.update { current ->
            current.copy(
                messages = current.messages + userMessage + aiMessage,
                isLoading = true
            )
        }

        // 启动协程流式监听
        viewModelScope.launch {
            try {
                sparkRepository.streamChat(text).collect { delta ->
                    _uiState.update { current ->
                        val updatedMessages = current.messages.map {
                            if (it.id == aiMessageId) {
                                it.copy(content = it.content + delta)
                            } else it
                        }
                        current.copy(messages = updatedMessages)
                    }
                }
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(isLoading = false, error = "AI 响应失败: ${e.message}")
                }
            } finally {
                _uiState.update { current ->
                    current.copy(isLoading = false)
                }
            }
        }
    }

    /**
     * 根据用户输入随机生成或选择一个 AI 回复
     */
//    private fun getRandomAiResponse(userInput: String): String {
//        // 简单地尝试匹配关键词，如果没有则随机选择
//        return when {
//            userInput.contains(
//                "compose",
//                ignoreCase = true
//            ) -> "Jetpack Compose 使得 UI 构建声明式且高效。"
//
//            userInput.contains(
//                "mvvm",
//                ignoreCase = true
//            ) -> "MVVM 在您的技术栈中是一个很好的选择，它有助于解耦视图和业务逻辑。"
//
//            userInput.contains("retrofit", ignoreCase = true) || userInput.contains(
//                "okhttp",
//                ignoreCase = true
//            ) -> "Retrofit 和 OkHttp 是进行网络请求的强大工具。"
//
//            userInput.contains(
//                "fastapi",
//                ignoreCase = true
//            ) -> "FastAPI 因其高性能和易用性而受到 Python 后端开发者的青睐。"
//
//            userInput.contains(
//                "redis",
//                ignoreCase = true
//            ) -> "Redis 可以为您的应用提供快速缓存和消息队列功能。"
//
//            userInput.contains(
//                "mariadb",
//                ignoreCase = true
//            ) -> "MariaDB 是一个稳定可靠的数据库，非常适合存储应用数据。"
//
//            userInput.contains(
//                "音视频",
//                ignoreCase = true
//            ) -> "音视频处理通常需要专业的 SDK 或自建流媒体服务，这会是服务端的一大挑战。"
//
//            else -> aiResponses.random()
//        }
//    }

    /**
     * 处理错误（例如网络请求失败）
     */
    fun handleError(errorMessage: String) {
        _uiState.update { currentState ->
            currentState.copy(error = errorMessage, isLoading = false)
        }
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(error = null)
        }
    }
}