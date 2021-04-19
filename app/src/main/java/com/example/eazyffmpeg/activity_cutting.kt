package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class activity_cutting : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    private var selectedVideoDurationInSecond = 0L
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutting)

        //UI Variables
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val btnProcess = findViewById<Button>(R.id.btnProcess)

        //File Dialog
        btnFileDialog.setOnClickListener()
        {
            //File Picker
            Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
        }

        btnProcess.setOnClickListener()
        {
            fadeInFadeOutProcess()
        }
    }

    //The FFMPEG Process
    private fun fadeInFadeOutProcess() {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)

        try {
            val query = FFmpegQueryExtension.videoFadeInFadeOut(txtFilePath.text.toString(), selectedVideoDurationInSecond, fadeInEndSeconds = 3, fadeOutStartSeconds = 3,
                output = Common.getFilePath(this, Common.VIDEO)
            )

            CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
                override fun process(logMessage: LogMessage) {
                    txtFilePath.text = logMessage.text
                }

                override fun success() {
                    txtFilePath.text = "Success"
                }

                override fun cancel() {
                    txtFilePath.text = "Cancel"
                }

                override fun failed() {
                    txtFilePath.text = "Failed"
                }

            })
        }catch (e: Exception){}
    }


    //Get The Length of the Video
    @SuppressLint("NewApi")
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        val txtFilePath= findViewById<TextView>(R.id.txtFilePath)

        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    isInputVideoSelected = true
                    CompletableFuture.runAsync {
                        retriever = MediaMetadataRetriever()
                        retriever?.setDataSource(txtFilePath.text.toString())
                        val time = retriever?.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
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

    //Get the File Path from the Picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            selectedFiles(mediaFiles,requestCode)

            val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
            if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                txtFilePath.text = (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
            }
        }
    }
}