package com.example.interviewhelper.viewmodel

import android.Manifest
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewhelper.data.repository.SparkChainRepository
import com.example.interviewhelper.data.repository.SparkRepository
import com.example.interviewhelper.data.repository.WebSocketRepository
import com.example.interviewhelper.utils.H264Encoder
import com.example.interviewhelper.utils.LoadingDialogController
import com.example.interviewhelper.utils.toHexString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewController @Inject constructor(
    private val websocket: WebSocketRepository,
    private val sparkRepository: SparkRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val number: Int = savedStateHandle.get<Int>("number") ?: 10
    val subject: String = savedStateHandle.get<String>("subject") ?: ""

    val micSwitch = mutableStateOf(true)
    val videoSwitch = mutableStateOf(true)

    private val _previewView = MutableLiveData<PreviewView?>()
    val previewView: LiveData<PreviewView?> = _previewView
    private var cameraProvider: ProcessCameraProvider? = null

    val isTalking = mutableStateOf(false)
    val questions = mutableListOf<String>()

    val speechWordsList = mutableListOf<String>()

    private var audioRecord: AudioRecord? = null
    private var isRecordingAudio = false
    private var audioJob: Job? = null

    val wsMessages = MutableStateFlow("")

    init {

        //SparkChainRepository.initAsr()

        viewModelScope.launch(Dispatchers.IO) {
            initQuestions(subject = subject, number= number)
//            SparkChainRepository.wordFlow.collect {
//                speechWordsList.add(it)
//            }
//            websocket.connect().collect { msg ->
//                wsMessages.value = msg
//            }
        }
    }


    suspend fun initQuestions (subject: String, number: Int = 10) {
        LoadingDialogController.show("面试准备中")
        try {
            val response = sparkRepository.getQuestions(subject = subject, number = number)
            if (response.code == 200) {
                questions.clear()
                questions.addAll(response.data?.questions ?: emptyList())
            }
        } catch (e: Exception) {
            Log.e("init","questions init failed: $e")
        } finally {
            LoadingDialogController.hide()
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


            analyzer.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val image = imageProxy.image
                if (image != null) {
//                    val encoder = H264Encoder()
//                    val inputSurface = encoder.imageToNV21(image)
//                    Log.d("nv21", inputSurface.toHexString())
                    // websocket.sendAudioData(inputSurface)
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
            var count: Long = 0
            while (isRecordingAudio && audioRecord != null) {
                val read = audioRecord!!.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val audioData = buffer.copyOf(read)
                    Log.d("AudioSend", "发送音频数据，长度: ${audioData.size} 字节")
                    // SparkChainRepository.start(audioData, count++)
                    // websocket.sendAudioData(audioData)
                    Log.d("AudioSend", "发送音频数据，长度: ${audioData.size} 字节")
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
        stopAudioCapture()
        SparkChainRepository.clear()
    }
}