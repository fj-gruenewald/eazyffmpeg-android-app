package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.util.*

class activity_transform : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    var mediaFiles: List<MediaFile>? = null
    var aspectRatio: String = getString(R.string.placeholderString)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform)

        //UI Variables
        //Spinner
        val transformationSpinner = findViewById<Spinner>(R.id.transformationSpinner)
        val transformations = resources.getStringArray(R.array.transformations)

        //Button
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)
        val btnApplyTransform = findViewById<Button>(R.id.btnApplyTransform)

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

        //Do the Transformation
        btnApplyTransform.setOnClickListener {

            //check if a video is selected
            if (isInputVideoSelected == true) {
                //Do the right action for every effect
                when (transformationSpinner.selectedItem) {
                    "Rotaten 90" -> {
                        rotateDegree(90, true)
                    }
                    "Rotate 180" -> {
                        rotateDegree(180, true)
                    }
                    "Rotate 270" -> {
                        rotateDegree(270, true)
                    }
                    "Vertical Flip" -> {
                        rotateDegree(0, false)
                    }
                    "Aspect Ratio 16:9" -> {
                        aspectRatio = "16:9"
                        FFMPEG_AspectRatio()
                    }
                    "Aspect Ratio 4:3" -> {
                        aspectRatio = "4:3"
                        FFMPEG_AspectRatio()

                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.emptyVideoSelection), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //Fill the Spinner with the Transformations
        if (transformationSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, transformations
            )
            transformationSpinner.adapter = adapter

            //When something gets selected ==> print what got selected
            transformationSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@activity_transform,
                        getString(R.string.selected_item) + " " +
                                "" + transformations[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    //FFMPEG Process for Video Rotation
    private fun rotateDegree(degree: Int, isRotate: Boolean) {
        when {
            !isInputVideoSelected -> {
                Toast.makeText(this, getString(R.string.emptyVideoSelection), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                FFMPEG_Rotate(degree, isRotate)
            }
        }
    }

    private fun FFMPEG_Rotate(degree: Int, isRotate: Boolean) {
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query = if (isRotate) {
            FFmpegQueryExtension.rotateVideo(txtFilePath.text.toString(), degree, outputPath)
        } else {
            FFmpegQueryExtension.flipVideo(txtFilePath.text.toString(), degree, outputPath)
        }

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

    //FFMPEG Process for Aspect Ratio
    private fun FFMPEG_AspectRatio() {
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query =
            FFmpegQueryExtension.applyRatio(txtFilePath.text.toString(), aspectRatio, outputPath)

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

    //Get the Video to apply the Effect
    @SuppressLint("NewApi")
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    isInputVideoSelected = true
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
}