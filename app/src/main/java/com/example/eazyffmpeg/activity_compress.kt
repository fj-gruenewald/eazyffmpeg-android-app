package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.io.File
import java.util.*
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

        //File Dialog
        openFolderImageView.setOnClickListener(View.OnClickListener {
            //File Picker
            Common.selectFile(
                this,
                maxSelection = 1,
                isImageSelection = false,
                isAudioSelection = false
            )
        })

        //Start Process
        btnCompress.setOnClickListener(View.OnClickListener {
            compressProcess()
        })
    }

    //Get the Width and Height of the Video
    @SuppressLint("NewApi")                                              // tell if something is wrong with Lint
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {         //
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)              //
        when (requestCode) {                                                    //
            Common.VIDEO_FILE_REQUEST_CODE -> {                                 //
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {            //
                    isInputVideoSelected = true                                 //
                    CompletableFuture.runAsync {                                //
                        retriever = MediaMetadataRetriever()                    //
                        retriever?.setDataSource(txtFilePath.text.toString())   //
                        val bit = retriever?.frameAtTime                        //
                        width = bit?.width                                      //
                        height = bit?.height                                    //
                    }
                } else {                                                        //
                    Toast.makeText(                                             //
                        this,                                           //
                        getString(R.string.emptyVideoSelection),                                    //
                        Toast.LENGTH_SHORT                                      //
                    ).show()                                                    //
                }
            }
        }
    }

    //The FFMPEG Process
    private fun compressProcess() {                                                                 // Methode zum Komprimieren

        val txtInfo = findViewById<TextView>(R.id.txtInfo)                                          // initialisiere: Info Text Field
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)                                  // initialisiere: Path Text Field
        val outputPath = Common.getFilePath(this, Common.VIDEO)                             // initialisiere: Output Field

        val query =                                                                                 // FFmpeg Query
            FFmpegQueryExtension.compressor(txtFilePath.text.toString(), width, height, outputPath) // FFmpegQueryExtension der Library .ZuAusführenderProzess
        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {                    // Callback für Info Text
            override fun process(logMessage: LogMessage) {                                          // Prozess überschreiben für Info Text
                txtInfo.text = logMessage.text                                                      // Text Feld das upgedated werden soll
            }

            @SuppressLint("SetTextI18n")                                                     // Lokalisierung unterdrücken (eng text)
            override fun success() {                                                                // überschreibe success function von ffmpeg
                txtInfo.text = String.format(                                                       // Text Feld mit erfolgstext füllen
                    getString(R.string.ffmpegProcessSuccessfull), outputPath, Common.getFileSize(                        // text aus strings.xml + ausgabe + größe
                        File(outputPath)                                                            // es soll nur der text angezeigt werden
                    )
                )
            }

            override fun cancel() {                                                                 // Cancle wird überschrieben und es soll nichts passieren
            }

            override fun failed() {                                                                 // Failed wird überschrieben, hier soll später ein errorcode hin
            }
        })
    }

    //Get the File Path from the Picker
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