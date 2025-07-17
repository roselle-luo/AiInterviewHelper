package com.example.interviewhelper.utils // 假设你的包名

import android.media.Image
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import javax.inject.Inject

// 辅助函数：将字节数组转换为十六进制字符串，用于日志打印
fun ByteArray.toHexString(maxLength: Int = size): String {
    return this.copyOfRange(0, minOf(size, maxLength)).joinToString(" ") { "%02x".format(it) }
}

// 辅助函数：将 ByteBuffer 转换为十六进制字符串
fun ByteBuffer.toHexString(maxLength: Int = remaining()): String {
    val bytes = ByteArray(minOf(remaining(), maxLength))
    val originalPosition = position()
    get(bytes)
    position(originalPosition) // 恢复position
    return bytes.toHexString()
}


class H264Encoder @Inject constructor() {

    private val TAG = "H264VideoEncoder"

    private var mediaCodec: MediaCodec? = null
    private var isEncoderStarted = false

    // SPS/PPS 缓存，包含 NALU 起始码
    private var spsPpsNalus: ByteArray? = null
    private var spsPpsSetFlag = false

    private var frameCount = 0L // 用于生成时间戳

    // 调试用途：将编码后的H.264数据写入本地文件
    private var debugFileOutputStream: FileOutputStream? = null

    /**
     * 初始化编码器
     * @param width 视频宽度
     * @param height 视频高度
     * @param frameRate 帧率
     * @param bitRate 比特率 (bps)
     * @param debugOutputPath 如果提供，会将H.264裸流写入该文件，用于调试
     */
    fun initEncoder(
        width: Int,
        height: Int,
        frameRate: Int = 30,
        bitRate: Int = 2_000_000, // 2Mbps
        debugOutputPath: String? = null
    ): Boolean {
        if (isEncoderStarted) {
            Log.w(TAG, "Encoder already started.")
            return true
        }

        try {
            val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height)
            format.setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible // 或 COLOR_FormatYUV420SemiPlanar
            )
            format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
            format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate)
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // 1秒一个I帧，有助于流传输快速解码

            // 选择一个编码器 (可选，系统会自动选择最佳的)
            // val codecInfo = selectCodec(MediaFormat.MIMETYPE_VIDEO_AVC)
            // if (codecInfo == null) {
            //     Log.e(TAG, "No H.264 codec found!")
            //     return false
            // }
            // mediaCodec = MediaCodec.createByCodecName(codecInfo.name)

            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            mediaCodec?.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mediaCodec?.start()
            isEncoderStarted = true
            frameCount = 0L
            spsPpsSetFlag = false
            spsPpsNalus = null

            debugOutputPath?.let {
                try {
                    debugFileOutputStream = FileOutputStream(it)
                    Log.d(TAG, "H.264 debug output file created at: $it")
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to create debug output file: ${e.message}")
                    debugFileOutputStream = null
                }
            }

            Log.d(TAG, "H264 encoder initialized for ${width}x${height} @ ${frameRate}fps, ${bitRate}bps")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize H264 encoder: ${e.message}", e)
            releaseEncoder() // 确保在失败时释放资源
            return false
        }
    }

    /**
     * 编码 Image (YUV_420_888 格式) 为 H.264 帧。
     * @param image 从 CameraX ImageAnalysis 获取的 Image 对象
     * @return 编码后的 H.264 帧列表，每帧包含 NALU 起始码
     */
    fun encodeImageToH264(image: Image): List<ByteArray> {
        if (!isEncoderStarted || mediaCodec == null) {
            Log.e(TAG, "Encoder not started or is null.")
            image.close()
            return emptyList()
        }

        val encodedFrames = mutableListOf<ByteArray>()
        val inputBuffers = mediaCodec!!.inputBuffers // Android L (API 21) 及以上推荐使用 getInputBuffer(int)
        val outputBuffers = mediaCodec!!.outputBuffers // Android L (API 21) 及以上推荐使用 getOutputBuffer(int)
        val bufferInfo = MediaCodec.BufferInfo()

        try {
            // 1. 将 Image 数据转换为 NV21 或 YUV420Planar 格式并送入编码器
            val inputBufferId = mediaCodec!!.dequeueInputBuffer(10000) // 10ms timeout
            if (inputBufferId >= 0) {
                val inputBuffer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaCodec!!.getInputBuffer(inputBufferId)
                } else {
                    inputBuffers[inputBufferId]
                }
                inputBuffer?.clear()

                // 将 Image 转换为编码器所需的 YUV 格式。这里使用 NV21 转换。
                // 注意：如果 MediaCodec 设置的是 COLOR_FormatYUV420Flexible，
                // 那么输入可以是 NV12 (YV12) 或 NV21。NV21 是更常见的。
                val yuvData = imageToNV21(image) // 或者 imageToYUV420Planar(image)
                inputBuffer?.put(yuvData)

                // 使用 System.nanoTime() 作为时间戳，确保是单调递增的
                val presentationTimeUs = System.nanoTime() / 1000L
                mediaCodec!!.queueInputBuffer(inputBufferId, 0, yuvData.size, presentationTimeUs, 0)
                frameCount++
            } else {
                Log.w(TAG, "No input buffer available, dropping frame.")
            }

            // 2. 从编码器获取编码后的 H.264 数据
            var outputBufferId = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 0) // non-blocking

            while (outputBufferId >= 0) {
                val outputBuffer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaCodec!!.getOutputBuffer(outputBufferId)
                } else {
                    outputBuffers[outputBufferId]
                }

                if (outputBuffer != null && bufferInfo.size > 0) {
                    outputBuffer.position(bufferInfo.offset)
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size)

                    val encodedData = ByteArray(bufferInfo.size)
                    outputBuffer.get(encodedData)

                    // 检查并缓存 SPS/PPS NALUs
                    if (!spsPpsSetFlag) {
                        val outputFormat = mediaCodec!!.outputFormat
                        val csd0 = outputFormat.getByteBuffer("csd-0") // 通常包含 VPS, SPS, PPS for H.265 or SPS, PPS for H.264
                        val csd1 = outputFormat.getByteBuffer("csd-1") // H.264 通常是 PPS 的补充，H.265 可能是其他

                        val tempSpsPpsList = mutableListOf<ByteArray>()

                        csd0?.let {
                            val bytes = ByteArray(it.remaining())
                            it.get(bytes)
                            tempSpsPpsList.add(bytes)
                            Log.d(TAG, "Got csd-0 (SPS/PPS): size=${bytes.size}, first bytes=${bytes.toHexString()}")
                        }
                        csd1?.let { // 某些编码器会将 PPS 放在 csd-1，但 H.264 csd-0 通常已包含 SPS/PPS
                            val bytes = ByteArray(it.remaining())
                            it.get(bytes)
                            tempSpsPpsList.add(bytes)
                            Log.d(TAG, "Got csd-1: size=${bytes.size}, first bytes=${bytes.toHexString()}")
                        }

                        if (tempSpsPpsList.isNotEmpty()) {
                            spsPpsNalus = tempSpsPpsList.reduce { acc, bytes -> acc + bytes }
                            spsPpsSetFlag = true
                            Log.d(TAG, "Cached SPS/PPS NALUs. Total size: ${spsPpsNalus?.size}, first bytes: ${spsPpsNalus?.toHexString()}")
                        } else {
                            Log.w(TAG, "csd-0/csd-1 not available yet from outputFormat.")
                        }
                    }

                    // 判断是否为关键帧 (I-frame)
                    val isKeyFrame = (bufferInfo.flags and MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0

                    if (isKeyFrame && spsPpsNalus != null) {
                        // 关键帧前附加 SPS/PPS NALUs
                        val fullFrame = ByteArray(spsPpsNalus!!.size + encodedData.size)
                        System.arraycopy(spsPpsNalus!!, 0, fullFrame, 0, spsPpsNalus!!.size)
                        System.arraycopy(encodedData, 0, fullFrame, spsPpsNalus!!.size, encodedData.size)
                        encodedFrames.add(fullFrame)
                        Log.d(TAG, "Sent KEY_FRAME with cached SPS/PPS. Total size: ${fullFrame.size}, first bytes: ${fullFrame.toHexString(40)}")
                        debugFileOutputStream?.write(fullFrame) // 写入调试文件
                    } else {
                        // 非关键帧，但如果也需要附加SPS/PPS（通常H.264流中SPS/PPS只在关键帧前发送一次，或者通过ExtraData提供）
                        // 如果你的意图是每帧都包含SPS/PPS，那么这里也应该使用 fullFrame
                        // 如果不是，那么下面的代码是正确的，即P/B帧不包含SPS/PPS
                        val frameToWrite: ByteArray
                        if (spsPpsNalus != null) { // 检查spsPpsNalus是否可用，如果需要附加
                            val fullFrame = ByteArray(spsPpsNalus!!.size + encodedData.size)
                            System.arraycopy(spsPpsNalus!!, 0, fullFrame, 0, spsPpsNalus!!.size)
                            System.arraycopy(encodedData, 0, fullFrame, spsPpsNalus!!.size, encodedData.size)
                            frameToWrite = fullFrame
                            Log.d(TAG, "Sent P/B FRAME with cached SPS/PPS. Total size: ${fullFrame.size}, first bytes: ${fullFrame.toHexString(40)}")
                        } else {
                            frameToWrite = encodedData
                            Log.d(TAG, "Sent P/B FRAME (no SPS/PPS attached): size=${encodedData.size}, first bytes=${encodedData.toHexString()}")
                        }
                        encodedFrames.add(frameToWrite) // 添加 fullFrame 或 encodedData
                        debugFileOutputStream?.write(frameToWrite) // 写入 fullFrame 或 encodedData
                    }
                }


                mediaCodec!!.releaseOutputBuffer(outputBufferId, false)
                outputBufferId = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 0) // 继续获取下一个输出缓冲区
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error encoding image: ${e.message}", e)
        } finally {
            image.close() // 确保释放Image Proxy
        }
        return encodedFrames
    }

    /**
     * 将 CameraX 的 Image (YUV_420_888) 转换为 NV21 格式。
     * NV21: YYYYYYYY VU VU
     */
    fun imageToNV21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 2

        val nv21 = ByteArray(ySize + uvSize)

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val yRowStride = image.planes[0].rowStride
        val uvRowStride = image.planes[1].rowStride
        val uvPixelStride = image.planes[1].pixelStride

        var pos = 0

        // Y plane
        yBuffer.position(0)
        for (row in 0 until height) {
            if (yRowStride == width) { // 优化：如果步幅等于宽度，可以直接复制一行
                yBuffer.get(nv21, pos, width)
                pos += width
            } else { // 否则，逐字节复制
                yBuffer.position(row * yRowStride)
                yBuffer.get(nv21, pos, width)
                pos += width
            }
        }

        // UV planes (VU in NV21)
        val chromaHeight = height / 2
        val chromaWidth = width / 2
        var uvPos = ySize
        for (row in 0 until chromaHeight) {
            for (col in 0 until chromaWidth) {
                val uIndex = row * uvRowStride + col * uvPixelStride
                val vIndex = row * uvRowStride + col * uvPixelStride

                // NV21 格式是 VU 交错
                nv21[uvPos++] = vBuffer.get(vIndex)
                nv21[uvPos++] = uBuffer.get(uIndex)
            }
        }
        return nv21
    }


    /**
     * 释放编码器资源
     */
    fun releaseEncoder() {
        if (mediaCodec != null) {
            try {
                mediaCodec?.stop()
                mediaCodec?.release()
                Log.d(TAG, "H264 encoder released.")
            } catch (e: Exception) {
                Log.e(TAG, "Error releasing H264 encoder: ${e.message}", e)
            } finally {
                mediaCodec = null
                isEncoderStarted = false
                spsPpsSetFlag = false
                spsPpsNalus = null
                debugFileOutputStream?.close()
                debugFileOutputStream = null
            }
        }
    }
}