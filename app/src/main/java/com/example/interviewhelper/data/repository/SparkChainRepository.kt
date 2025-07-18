package com.example.interviewhelper.data.repository

import android.util.Log
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.asr.ASR
import com.iflytek.sparkchain.core.asr.ASR.ASRResult
import com.iflytek.sparkchain.core.asr.AsrCallbacks
import kotlinx.coroutines.flow.MutableStateFlow


object SparkChainRepository {

    private val asr = ASR("zh_cn", "iat", "mandarin")

    val wordFlow = MutableStateFlow<String>("")

    private val collectedWords = mutableListOf<String>()

    val mAsrCallbacks = object : AsrCallbacks {
        override fun onResult(asrResult: ASRResult, p1: Any) {
            val begin = asrResult.begin               // 识别结果起始点
            val end = asrResult.end                   // 识别结果结束点
            val status = asrResult.status             // 结果状态
            val result = asrResult.bestMatchText      // 识别结果文本
            val sid = asrResult.sid                   // 会话 ID

            val vads = asrResult.vads
            for (vad in vads) {
                val vadBegin = vad.begin              // VAD 起始点
                val vadEnd = vad.end                  // VAD 结束点
            }

            val transcriptions = asrResult.transcriptions
            for (transcription in transcriptions) {
                val segments = transcription.segments
                for (segment in segments) {
                    val word = segment.text           // 字词结果
                    Log.d("SparkSpeechRead", word)
                    collectedWords.add(word)
                }
            }
        }


        override fun onError(p0: ASR.ASRError?, p1: Any?) {
            Log.e("SparkSpeechRead", p0.toString())
        }

        override fun onBeginOfSpeech() {
            Log.d("SparkSpeechRead", "开始讲话")
        }

        override fun onEndOfSpeech() {
            Log.d("SparkSpeechRead", "结束讲话")
            wordFlow.value = collectedWords.toList().joinToString()
            Log.d("SparkSpeechRead(Full)", wordFlow.value)
        }
    }

    fun start(data: ByteArray, id: Long) {
        val ret: Int = asr.startListener(id.toString()) //入参为用户自定义标识，用户关联onResult结果。
        if (ret == 0) {
            Log.d("SparkSpeechRead", "正在录音")
            sendAudioData(data = data)
        } else {
            Log.e("SparkSpeechRead", "录音开启失败：${ret.toString()}")
        }
    }

    fun sendAudioData(data: ByteArray) {
        asr.write(data)
    }

    fun initAsr() {
        asr.registerCallbacks(mAsrCallbacks)
    }

    fun clear() {
        asr.stop(false)
    }

}