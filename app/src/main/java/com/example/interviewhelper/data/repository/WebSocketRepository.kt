package com.example.interviewhelper.data.repository

import android.util.Log
import com.example.interviewhelper.utils.H264Encoder
import com.example.interviewhelper.utils.toHexString
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.toByteString
import javax.inject.Inject

class WebSocketRepository @Inject constructor(
) {

    private val WS_URL = "ws://192.168.2.63:8000/wb/video"
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    // 建立连接，消息以 Flow 形式发出
    fun connect(): Flow<String> = callbackFlow {
        val request = Request.Builder().url(WS_URL).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
            }

            override fun onMessage(ws: WebSocket, text: String) {
                trySend(text)
            }

            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                // 如果需要处理二进制消息，这里可扩展
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                ws.close(code, reason)
                close()
            }
        }
        webSocket = client.newWebSocket(request, listener)

        // 关闭时取消连接
        awaitClose {
            webSocket?.close(1000, "Closing")
            client.dispatcher.executorService.shutdown()
        }
    }

    // 发送视频数据，协议要求加0x01前缀
    fun sendVideoData(data: ByteArray) {
        val sendData = byteArrayOf(0x01) + data
        webSocket?.send(ByteString.of(*sendData))
    }

    // 发送音频数据，协议要求加0x02前缀
    fun sendAudioData(data: ByteArray) {
        val sendData = byteArrayOf(0x02) + data
        Log.d("audio_data", sendData.toHexString())
        webSocket?.send(sendData.toByteString())
    }
}