package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.io.File
import java.util.ArrayList
import java.util.concurrent.CompletableFuture

class activity_compress : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null
    var height: Int? = 0
    var width: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress)

        //UI Variables
        val openFolderImageView = findViewById<ImageView>(R.id.btnFileDialog)
        val btnCompress = findViewById<Button>(R.id.btnCompress)
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        //File Dialog
        openFolderImageView.setOnClickListener(View.OnClickListener {
            //File Picker
            Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
        })

        //Start Process
        btnCompress.setOnClickListener(View.OnClickListener {
            compressProcess()
        })
    }

    //Get the Width and Height of the Video
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
                        val bit = retriever?.frameAtTime
                        width = bit?.width
                        height = bit?.height
                    }
                } else {
                    Toast.makeText(this, "no video selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //The FFMPEG Process
    private fun compressProcess() {

        val txtInfo = findViewById<TextView>(R.id.txtInfo)
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val outputPath = Common.getFilePath(this, Common.VIDEO)

        val query = FFmpegQueryExtension.compressor(txtFilePath.text.toString(), width, height, outputPath)
        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                txtInfo.text = logMessage.text
            }

            @SuppressLint("SetTextI18n")
            override fun success() {
                txtInfo.text = String.format("Sucessful: ", outputPath, Common.getFileSize(
                    File(outputPath)
                ))
            }

            override fun cancel() {
            }

            override fun failed() {
            }
        })
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