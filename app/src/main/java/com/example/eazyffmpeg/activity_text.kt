package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.util.*
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
        val txtInputText = findViewById<EditText>(R.id.txtInputText)
        val txtXInput = findViewById<EditText>(R.id.txtXInput)
        val txtYInput = findViewById<EditText>(R.id.txtYInput)

        //Strings
        val text_color = resources.getStringArray(R.array.text_color)
        val text_size = resources.getStringArray(R.array.text_size)

        //Open the File Dialog
        btnFileDialog.setOnClickListener {
            //File Picker
            Common.selectFile(
                this,
                maxSelection = 1,
                isImageSelection = false,
                isAudioSelection = false
            )
        }

        //Do the Text Add process
        btnAddTextToVideo.setOnClickListener {
            if (activityTextValidation(txtInputText.text.toString(),txtXInput.text.toString(),txtYInput.text.toString()))
            {
                FFMPEG_AddTextToVideo()
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
        when (sizeSpinner.selectedItem) {
            "Small" -> {
                textSize = 68
            }
            "Medium" -> {
                textSize = 108
            }
            "Large" -> {
                textSize = 148
            }
        }

        //build the color
        val colorSpinner = findViewById<Spinner>(R.id.colorSpinner)
        when (colorSpinner.selectedItem) {
            "White" -> {
                textColor = getString(R.string.colorWhite)
            }
            "Black" -> {
                textColor = getString(R.string.colorBlack)
            }
            "Red" -> {
                textColor = getString(R.string.colorRed)
            }
            "Blue" -> {
                textColor = getString(R.string.colorBlue)
            }
            "Green" -> {
                textColor = getString(R.string.colorGreen)
            }
            "Yellow" -> {
                textColor = getString(R.string.colorYellow)
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
        val fontPath = Common.getFileFromAssets(this, getString(R.string.standardFont)).absolutePath

        //do the process
        var accept: Boolean = true
        val query = FFmpegQueryExtension.addTextOnVideo(
            txtFilePath.text.toString(),
            txtInputText.text.toString(),
            xPos,
            yPos,
            fontPath = fontPath,
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
                txtInfo.text =
                    String.format(getString(R.string.ffmpegProcessSuccessfull), outputPath)
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
                    Toast.makeText(
                        this,
                        getString(R.string.emptyVideoSelection),
                        Toast.LENGTH_SHORT
                    ).show()
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

    //validation for the activity inputs
    fun activityTextValidation(txtInput:String, xInput:String, yInput:String ): Boolean
    {
        when {
            !isInputVideoSelected -> {
                Toast.makeText(
                    this,
                    getString(R.string.emptyVideoSelection),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            TextUtils.isEmpty(txtInput) -> {
                Toast.makeText(this, getString(R.string.emptyInputText), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(xInput) -> {
                Toast.makeText(this, getString(R.string.emptyXPosition), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            xInput.toFloat() > 100 || xInput
                .toFloat() <= 0 -> {
                Toast.makeText(this, getString(R.string.xPositionNotValid), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(yInput) -> {
                Toast.makeText(this, getString(R.string.emptyYPosition), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            yInput.toFloat() > 100 || yInput
                .toFloat() <= 0 -> {
                Toast.makeText(this, getString(R.string.yPositionNotValid), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            else -> {
                return true
            }
        }
    }
}