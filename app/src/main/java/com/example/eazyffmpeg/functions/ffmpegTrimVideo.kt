package com.example.eazyffmpeg.functions

import android.content.Context
import com.example.eazyffmpeg.functions.types.OutputType
import com.example.eazyffmpeg.functions.helpers.Utilities
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
import java.io.IOException

class ffmpegTrimVideo private constructor(private val context: Context) {

    private var video: File? = null
    private var callback: ffmpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var startTime = "00:00:00"
    private var endTime = "00:00:00"

    fun setFile(originalFiles: File): ffmpegTrimVideo {
        this.video = originalFiles
        return this
    }

    fun setCallback(callback: ffmpegCallback): ffmpegTrimVideo {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): ffmpegTrimVideo {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): ffmpegTrimVideo {
        this.outputFileName = output
        return this
    }

    fun setStartTime(startTime: String): ffmpegTrimVideo {
        this.startTime = startTime
        return this
    }

    fun setEndTime(endTime: String): ffmpegTrimVideo {
        this.endTime = endTime
        return this
    }

    fun trim() {

        if (video == null || !video!!.exists()) {
            callback!!.onFailure(IOException("File not exists"))
            return
        }
        if (!video!!.canRead()) {
            callback!!.onFailure(IOException("Can't read the file. Missing permission?"))
            return
        }

        val outputLocation = Utilities.getConvertedFile(outputPath, outputFileName)

        val cmd = arrayOf("-i", video!!.path, "-ss", startTime, "-t", endTime, "-c", "copy", outputLocation.path)

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback!!.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    Utilities.refreshGallery(outputLocation.path, context)
                    callback!!.onSuccess(outputLocation, OutputType.TYPE_VIDEO)

                }

                override fun onFailure(message: String?) {
                    if (outputLocation.exists()) {
                        outputLocation.delete()
                    }
                    callback!!.onFailure(IOException(message))
                }

                override fun onFinish() {
                    callback!!.onFinish()
                }
            })
        } catch (e: Exception) {
            callback!!.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback!!.onNotAvailable(e2)
        }

    }

    companion object {

        val TAG = "VideoTrimmer"

        fun with(context: Context): ffmpegTrimVideo {
            return ffmpegTrimVideo(context)
        }
    }
}