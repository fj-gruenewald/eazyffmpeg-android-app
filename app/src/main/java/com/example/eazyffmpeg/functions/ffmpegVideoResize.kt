package com.example.eazyffmpeg.functions

import android.content.Context
import android.widget.Toast
import com.example.eazyffmpeg.activity_converter
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.example.eazyffmpeg.functions.helpers.Utilities
import java.io.File
import java.io.IOException

class ffmpegVideoResize private constructor(private val context: Context) {

    private var video: File? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var size = ""

    fun setFile(originalFiles: File): ffmpegVideoResize {
        this.video = originalFiles
        return this
    }


    fun setOutputPath(output: String): ffmpegVideoResize {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): ffmpegVideoResize {
        this.outputFileName = output
        return this
    }

    fun setSize(output: String): ffmpegVideoResize {
        this.size = output
        return this
    }

    fun resize() {

        if (video == null || !video!!.exists()) {
            //callback!!.onFailure(IOException("File not exists"))
            return
        }
        if (!video!!.canRead()) {
            //callback!!.onFailure(IOException("Can't read the file. Missing permission?"))
            return
        }

        val outputLocation = Utilities.getConvertedFile(outputPath, outputFileName)

        val cmd = arrayOf("-i", video!!.path, "-vf", "scale=" + size, outputLocation.path, "-hide_banner")

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}


            })
        } catch (e: Exception) {
        }
    }
}
