package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.io.File
import java.util.ArrayList
import java.util.concurrent.CompletableFuture

class activity_text : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null
    var height: Int? = 0
    var width: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)


        //Make the UI Variables
        //Spinner
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)
        val colorSpinner = findViewById<Spinner>(R.id.colorSpinner)

        //Buttons
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)
        val btnAddTextToVideo = findViewById<Button>(R.id.btnAddTextToVideo)

        //Text
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)
        val txtInputText = findViewById<EditText>(R.id.txtInputText)
        val txtXInput = findViewById<EditText>(R.id.txtXInput)
        val txtYInput = findViewById<EditText>(R.id.txtYInput)

        //Strings
        val text_color = resources.getStringArray(R.array.text_color)
        val text_size = resources.getStringArray(R.array.text_size)

        //Open the File Dialog
        btnFileDialog.setOnClickListener() {
            //File Picker
            Common.selectFile(
                this,
                maxSelection = 1,
                isImageSelection = false,
                isAudioSelection = false
            )
        }

        //Do the Text Add process
        btnAddTextToVideo.setOnClickListener() {
            when{
                !isInputVideoSelected -> {
                    Toast.makeText(this, "no video selected", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(txtInputText.text.toString()) -> {
                    Toast.makeText(this, "no input text set", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(txtXInput.text.toString()) -> {
                    Toast.makeText(this, "no x postion", Toast.LENGTH_SHORT).show()
                }
                txtXInput.text.toString().toFloat() > 100 || txtXInput.text.toString().toFloat() <= 0 -> {
                    Toast.makeText(this, "x position value invalid", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(txtYInput.text.toString()) -> {
                    Toast.makeText(this, "no y position", Toast.LENGTH_SHORT).show()
                }
                txtYInput.text.toString().toFloat() > 100 || txtYInput.text.toString().toFloat() <= 0 -> {
                    Toast.makeText(this, "y position invalid", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    FFMPEG_AddTextToVideo()
                }
            }
        }


        //Check the Location Spinner --> fill em
        if (colorSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, text_color
            )
            colorSpinner.adapter = adapter

            //When something gets selected ==> print what got selected
            colorSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@activity_text,
                        getString(R.string.selected_item) + " " +
                                "" + text_color[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        //Check the Size Spinner --> fill em
        if (sizeSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, text_size
            )
            sizeSpinner.adapter = adapter

            //When something gets selected ==> print what got selected
            sizeSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@activity_text,
                        getString(R.string.selected_item) + " " +
                                "" + text_size[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    //The add text ffmpeg process
    private fun FFMPEG_AddTextToVideo() {
        //temp vars
        var textSize = 28
        var textColor: String = "none"

        //text fields
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        //input fields
        val txtInputText = findViewById<EditText>(R.id.txtInputText)
        val txtXInput = findViewById<EditText>(R.id.txtXInput)
        val txtYInput = findViewById<EditText>(R.id.txtYInput)

        //build the text size
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)
        when(sizeSpinner.selectedItem){
            "Small" ->{
                textSize = 68
            }
            "Medium" ->{
                textSize = 108
            }
            "Large" ->{
                textSize = 148
            }
        }

        //build the color
        val colorSpinner = findViewById<Spinner>(R.id.colorSpinner)
        when(colorSpinner.selectedItem){
            "White" ->{
                textColor = "white"
            }
            "Black" ->{
                textColor = "black"
            }
            "Red" ->{
                textColor = "red"
            }
            "Blue" ->{
                textColor = "blue"
            }
            "Green" ->{
                textColor = "green"
            }
            "Yellow" ->{
                textColor = "green"
            }
        }

        //get the output path
        val outputPath = Common.getFilePath(this, Common.VIDEO)

        //test the positions
        val xPos = width?.let {
            (txtXInput.text.toString().toFloat().times(it)).div(100)
        }
        val yPos = height?.let {
            (txtYInput.text.toString().toFloat().times(it)).div(100)
        }

        //get the Path
        val fontPath = Common.getFileFromAssets(this, "ShortBaby.ttf").absolutePath

        //do the process
        var accept: Boolean = true
        val query = FFmpegQueryExtension.addTextOnVideo(txtFilePath.text.toString(), txtInputText.text.toString(), xPos, yPos, fontPath = fontPath,
            isTextBackgroundDisplay = true,
            fontSize = textSize,
            fontcolor = textColor,
            output = outputPath
        )
        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                txtInfo.text = logMessage.text
            }

            override fun success() {
                txtInfo.text = String.format("Successfull", outputPath)
            }

            override fun cancel() {
            }

            override fun failed() {
            }
        })
    }

    //Get the Video file to apply text
    @SuppressLint("NewApi")
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)

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