package com.example.eazyffmpeg.functions

import android.content.Context
import com.example.eazyffmpeg.functions.types.OutputType
import com.example.eazyffmpeg.functions.helpers.Utilities
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
import java.io.IOException

class ffmpegTextOnVideo private constructor(private val context: Context) {

    private var video: File? = null
    private var callback: ffmpegCallback? = null

    private var outputPath = ""
    private var outputFileName = ""
    private var font: File? = null
    private var text: String? = null
    private var position: String? = null
    private var color: String? = null
    private var size: String? = null
    private var border: String? = null
    private var addBorder: Boolean? = null

    //Border
    var BORDER_FILLED = ": box=1: boxcolor=black@0.5:boxborderw=5"
    var BORDER_EMPTY = ""

    fun setFile(originalFiles: File): ffmpegTextOnVideo {
        this.video = originalFiles
        return this
    }

    fun setOutputPath(output: String): ffmpegTextOnVideo {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): ffmpegTextOnVideo {
        this.outputFileName = output
        return this
    }

    fun setFont(output: File): ffmpegTextOnVideo {
        this.font = output
        return this
    }

    fun setText(output: String): ffmpegTextOnVideo {
        this.text = output
        return this
    }

    fun setPosition(output: String): ffmpegTextOnVideo {
        this.position = output
        return this
    }

    fun setColor(output: String): ffmpegTextOnVideo {
        this.color = output
        return this
    }

    fun setSize(output: String): ffmpegTextOnVideo {
        this.size = output
        return this
    }

    fun addBorder(output: Boolean): ffmpegTextOnVideo {
        if (output)
            this.border = BORDER_FILLED
        else
            this.border = BORDER_EMPTY
        return this
    }

    fun draw() {

        if (video == null || !video!!.exists()) {
            callback!!.onFailure(IOException("File not exists"))
            return
        }
        if (!video!!.canRead()) {
            callback!!.onFailure(IOException("Can't read the file. Missing permission?"))
            return
        }

        val outputLocation = Utilities.getConvertedFile(outputPath, outputFileName)

        val cmd = arrayOf("-i", video!!.path, "-vf", "drawtext=fontfile=" + font!!.path + ":text=" + text + ": fontcolor=" + color + ": fontsize=" + size + border + ": " + position, "-c:v", "libx264", "-c:a", "copy", "-movflags", "+faststart", outputLocation.path)

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

        val TAG = "TextOnVideo"

        fun with(context: Context): ffmpegTextOnVideo {
            return ffmpegTextOnVideo(context)
        }

        //Positions
        var POSITION_BOTTOM_RIGHT = "x=w-tw-10:y=h-th-10"
        var POSITION_TOP_RIGHT = "x=w-tw-10:y=10"
        var POSITION_TOP_LEFT = "x=10:y=10"
        var POSITION_BOTTOM_LEFT = "x=10:h-th-10"
        var POSITION_CENTER_BOTTOM = "x=(main_w/2-text_w/2):y=main_h-(text_h*2)"
        var POSITION_CENTER_ALLIGN = "x=(w-text_w)/2: y=(h-text_h)/3"
    }
}