package com.example.interviewhelper.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewhelper.data.repository.WebSocketRepository
import com.example.interviewhelper.utils.H264Encoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InterviewController @Inject constructor(
    private val websocket: WebSocketRepository,
    private val h256Coder: H264Encoder
) : ViewModel() {

    val micSwitch = mutableStateOf(true)
    val videoSwitch = mutableStateOf(true)

    private val _previewView = MutableLiveData<PreviewView?>()
    val previewView: LiveData<PreviewView?> = _previewView
    private var cameraProvider: ProcessCameraProvider? = null

    val isTalking = mutableStateOf(false)
    val questions = mutableListOf<String>("请说一下TCP和UDP通信协议的区别和各自的特点", "请讲述一下http和https协议的不同之处", "请详细描述一下安卓的Handler通信机制", "请说一下TCP和UDP通信协议的区别和各自的特点", "请讲述一下http和https协议的不同之处", "请详细描述一下安卓的Handler通信机制")

    private var audioRecord: AudioRecord? = null
    private var isRecordingAudio = false
    private var audioJob: Job? = null

    val wsMessages = MutableStateFlow("")

    init {
        viewModelScope.launch {
            websocket.connect().collect { msg ->
                wsMessages.value = msg
            }
        }
    }


    @ExperimentalGetImage
    fun startCamera(lifecycleOwner: LifecycleOwner, context: Context) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // 初始化预览
            val preview = Preview.Builder().build()
            val previewView = PreviewView(context)
            preview.surfaceProvider = previewView.surfaceProvider

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val debugOutputPath = File(context.getExternalFilesDir(null), "debug_client_${System.currentTimeMillis()}.h264").absolutePath
            // 初始化编码器
            val encoderInitialized = h256Coder.initEncoder(
                width = 1280, // 根据你的需求设置
                height = 720, // 根据你的需求设置
                frameRate = 30,
                bitRate = 2_000_000,
                debugOutputPath = debugOutputPath // 传入调试文件路径
            )
            if (!encoderInitialized) {
                Log.e("InterviewController", "Failed to initialize H264 video encoder!")
                return@addListener
            }

            analyzer.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val image = imageProxy.image
                if (image != null) {
//                    val h264Frames: List<ByteArray> = h256Coder.encodeImageToH264(image)
//                    for (frame in h264Frames) {
//                        websocket.sendVideoData(frame)
//                    }
                }
                imageProxy.close()
            }

            // 选择摄像头
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)  // 前置摄像头
                .build()

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    analyzer
                )
                _previewView.postValue(previewView)
                Log.d("CameraX", "PreviewView created: $previewView")
            } catch (exc: Exception) {
                Log.e("CameraViewModel", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startAudioCapture() {
        if (audioRecord != null && isRecordingAudio) {
            Log.d("WebSocketViewModel", "音频录制已在进行中。")
            return
        }

        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        if (bufferSize == AudioRecord.ERROR_BAD_VALUE || bufferSize == AudioRecord.ERROR) {
            Log.e("WebSocketViewModel", "获取音频缓冲区大小失败")
            return
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            Log.e("WebSocketViewModel", "AudioRecord 初始化失败")
            audioRecord?.release()
            audioRecord = null
            return
        }

        isRecordingAudio = true
        audioRecord?.startRecording()

        audioJob = viewModelScope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            while (isRecordingAudio && audioRecord != null) {
                val read = audioRecord!!.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val audioData = buffer.copyOf(read)
                    websocket.sendAudioData(audioData)
                    Log.d("WebSocketViewModel", "发送音频数据，长度: ${audioData.size} 字节")
                } else if (read == AudioRecord.ERROR_INVALID_OPERATION || read == AudioRecord.ERROR_BAD_VALUE) {
                    Log.e("WebSocketViewModel", "AudioRecord 读取错误: $read")
                    stopAudioCapture()
                    break
                }
            }
        }
    }

    fun stopAudioCapture() {
        isRecordingAudio = false
        audioJob?.cancel()
        audioJob = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }


    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }
}