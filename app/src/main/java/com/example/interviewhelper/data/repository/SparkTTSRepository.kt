package com.example.interviewhelper.data.repository

import android.util.Log
import com.iflytek.sparkchain.core.tts.OnlineTTS
import com.iflytek.sparkchain.core.tts.TTS
import com.iflytek.sparkchain.core.tts.TTSCallbacks
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.ByteArrayOutputStream

object SparkTTSRepository {

    private val tts = OnlineTTS("x4_lingfeizhe_emo")

    private val audioBuffer = ByteArrayOutputStream()

    val wordFlow = MutableStateFlow<ByteArray>(byteArrayOf(0))

    fun init() = tts.apply {
        aue("raw")
        bgs(0)
        registerCallbacks(ttsCallback)
    }


    @OptIn(ExperimentalStdlibApi::class)
    val ttsCallback = object : TTSCallbacks {

        override fun onResult(result: TTS.TTSResult?, p1: Any?) {
            val audio: ByteArray? = result?.data //音频数据
            val len: Int = result?.len ?: 0 //音频数据长度
            val status: Int = result?.status ?: 0 //数据状态
            val ced: String? = result?.ced //进度
            val sid: String? = result?.sid //sid

            audio?.let {
                audioBuffer.write(audio, 0, len)
                Log.d("Spark", audio.toHexString())
            }

            if (status == 2) {
                // 一句话合成完，写入 Flow
                val newAudio = audioBuffer.toByteArray()
                audioBuffer.reset()
                // 更新 Flow
                Log.d("Spark", "一句话结束：$newAudio")
                wordFlow.value = newAudio
            }
        }

        override fun onError(ttsError: TTS.TTSError?, p1: Any?) {
            val errCode: Int = ttsError?.code ?: -1 //错误码
            val errMsg: String? = ttsError?.errMsg //错误信息
            val sid: String? = ttsError?.sid //sid
            Log.e("Spark", "合成语音错误：$errCode: { $errMsg }")
        }
    }

    fun start(text: String) {
        Log.d("Spark", "开始启动1")
        tts.aRun(text)
    }

    fun stop() = tts.stop()
}