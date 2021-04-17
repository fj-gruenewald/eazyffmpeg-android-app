package com.example.eazyffmpeg.functions

import android.content.Context
import com.example.eazyffmpeg.functions.types.OutputType
import com.example.eazyffmpeg.functions.helpers.Utilities
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
import java.io.IOException

class ffmpegAudioOnVideo private constructor(private val context: Context) {

    private var audio: File? = null
    private var video: File? = null
    private var callback: ffmpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""

    fun setAudioFile(originalFiles: File): ffmpegAudioOnVideo {
        this.audio = originalFiles
        return this
    }

    fun setVideoFile(originalFiles: File): ffmpegAudioOnVideo {
        this.video = originalFiles
        return this
    }

    fun setOutputPath(output: String): ffmpegAudioOnVideo {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): ffmpegAudioOnVideo {
        this.outputFileName = output
        return this
    }

    fun merge() {

        if (audio == null || !audio!!.exists() || video == null || !video!!.exists()) {
            callback!!.onFailure(IOException("File not exists"))
            return
        }
        if (!audio!!.canRead() || !video!!.canRead()) {
            callback!!.onFailure(IOException("Can't read the file. Missing permission?"))
            return
        }

        val outputLocation = Utilities.getConvertedFile(outputPath, outputFileName)

        val cmd = arrayOf("-i", video!!.path, "-i", audio!!.path, "-c:v", "copy", "-c:a", "aac", "-strict", "experimental", "-map", "0:v:0", "-map", "1:a:0", "-shortest", outputLocation.path)

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

        val TAG = "AudioVideoMerger"

        fun with(context: Context): ffmpegAudioOnVideo {
            return ffmpegAudioOnVideo(context)
        }
    }
}