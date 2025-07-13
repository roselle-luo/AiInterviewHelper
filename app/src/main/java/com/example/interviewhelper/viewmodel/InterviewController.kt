package com.example.interviewhelper.viewmodel

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewController @Inject constructor() : ViewModel() {

    val micSwitch = mutableStateOf(true)
    val videoSwitch = mutableStateOf(true)

    private val _previewView = MutableLiveData<PreviewView?>()
    val previewView: LiveData<PreviewView?> = _previewView

    private var cameraProvider: ProcessCameraProvider? = null

    fun startCamera(lifecycleOwner: LifecycleOwner, context: Context) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            val previewView = PreviewView(context)
            preview.surfaceProvider = previewView.surfaceProvider

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)  // 前置摄像头
                .build()

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
                _previewView.postValue(previewView)
                Log.d("CameraX", "PreviewView created: $previewView")
            } catch (exc: Exception) {
                // 绑定失败，处理异常
                Log.e("CameraViewModel", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }
}