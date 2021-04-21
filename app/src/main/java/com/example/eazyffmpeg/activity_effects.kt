package com.example.eazyffmpeg

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import org.w3c.dom.Text
import java.io.File
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class activity_effects : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    private var selectedVideoDurationInSecond = 0L
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null
    var isSlowMo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_effects)

        //UI Variables
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)

        val effectsSpinner = findViewById<Spinner>(R.id.effectsSpinner)
        val btnApplyEffect = findViewById<Button>(R.id.btnApplyEffect)

        val effects = resources.getStringArray(R.array.effects)

        //Open File Dialog Button
        btnFileDialog.setOnClickListener() {
            //File Picker
            Common.selectFile(
                this,
                maxSelection = 1,
                isImageSelection = false,
                isAudioSelection = false
            )
        }

        //The Main Process
        btnApplyEffect.setOnClickListener() {

            //check if a video is selected
            if (isInputVideoSelected == true) {
                //Do the right action for every effect
                when (effectsSpinner.selectedItem) {
                    "fade-in-fade-out" -> {
                        FFMPEG_Fade()
                    }
                    "slow-motion" -> {
                        isSlowMo = true
                        FFMPEG_Motion()
                    }
                    "fast-motion" -> {
                        isSlowMo = false
                        FFMPEG_Motion()
                    }
                    "reverse" -> {
                        FFMPEG_Reverse()
                    }
                }
            }
            else{
                Toast.makeText(this, "no video selected", Toast.LENGTH_SHORT).show()
            }

        }

        //Check if Spinner is empty ==> give him some strings
        if (effectsSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, effects
            )
            effectsSpinner.adapter = adapter

            //When something gets selected ==> print what got selected
            effectsSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@activity_effects,
                        getString(R.string.selected_item) + " " +
                                "" + effects[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    //The FFMPEG Process
    private fun FFMPEG_Fade() {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        try {
            val query = FFmpegQueryExtension.videoFadeInFadeOut(
                txtFilePath.text.toString(),
                selectedVideoDurationInSecond,
                fadeInEndSeconds = 3,
                fadeOutStartSeconds = 3,
                output = Common.getFilePath(this, Common.VIDEO)
            )

            CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
                override fun process(logMessage: LogMessage) {
                    txtInfo.text = logMessage.text
                }

                override fun success() {
                    txtInfo.text = "Success"
                }

                override fun cancel() {
                }

                override fun failed() {
                }

            })
        } catch (e: Exception) {
        }
    }

    private fun FFMPEG_Reverse() {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query = FFmpegQueryExtension.videoReverse(txtFilePath.text.toString(), true, outputPath)

        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                txtInfo.text = logMessage.text
            }

            override fun success() {
                txtInfo.text = String.format("Success", outputPath)

            }

            override fun cancel() {

            }

            override fun failed() {

            }

        })
    }

    private fun FFMPEG_Motion() {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        var setpts = 0.5
        var atempo = 2.0
        if (isSlowMo) {
            setpts = 2.0
            atempo = 0.5
        }
        val query = FFmpegQueryExtension.videoMotion(txtFilePath.text.toString(), outputPath, setpts, atempo)
        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                txtInfo.text = logMessage.text
            }

            override fun success() {
                txtInfo.text = String.format("Success", outputPath)

            }

            override fun cancel() {

            }

            override fun failed() {

            }
        })
    }


    //Get The Length of the Video
    @SuppressLint("NewApi")
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)

        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    isInputVideoSelected = true
                    CompletableFuture.runAsync {
                        retriever = MediaMetadataRetriever()
                        retriever?.setDataSource(txtFilePath.text.toString())
                        val time =
                            retriever?.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        time?.toLong()?.let {
                            selectedVideoDurationInSecond = TimeUnit.MILLISECONDS.toSeconds(it)
                        }
                    }
                } else {
                    Toast.makeText(this, "no video", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Catch the File Dialog and Codec Spinner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            selectedFiles(mediaFiles, requestCode)

            val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
            if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                txtFilePath.text = (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
            }
        }
    }

}