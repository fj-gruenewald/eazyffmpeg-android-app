package com.example.eazyffmpeg.functions

import java.io.File

interface ffmpegCallback {

    fun onProgress(progress: String)

    fun onSuccess(convertedFile: File, type: String)

    fun onFailure(error: Exception)

    fun onNotAvailable(error: Exception)

    fun onFinish()
}