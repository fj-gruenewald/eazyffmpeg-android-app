package com.example.eazyffmpeg

import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.Common
import java.util.ArrayList

class activity_creation : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    private var isInputImageSelected: Boolean = false
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null

    //OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)

        //UI Variables
        //Spinner
        val methodSpinner = findViewById<Spinner>(R.id.methodSpinner)
        val methods = resources.getStringArray(R.array.creation_Methods)

        //Buttons
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)
        val btnImageDialog = findViewById<ImageView>(R.id.btnImageFileDialog)
        val btnCreateVideo = findViewById<Button>(R.id.btnCreateVideo)

        //Text
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtImageFilePath = findViewById<EditText>(R.id.txtImageFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        //Check if Spinner is empty ==> give him some strings
        if (methodSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, methods
            )
            methodSpinner.adapter = adapter

            //When something gets selected ==> print what got selected
            methodSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    //resetting the UI when a new spinner topic is choosen
                    txtFilePath.text.clear()
                    txtImageFilePath.text.clear()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        //Button Video FIle Dialog
        btnFileDialog.setOnClickListener() {
            when(methodSpinner.selectedItem){
                "video from videos"-> {
                    //File Picker for multiple videos
                    Common.selectFile(
                        this,
                        maxSelection = 5,
                        isImageSelection = false,
                        isAudioSelection = false
                    )
                }
                "video from video and image"-> {
                    //File Picker for a single video
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = false,
                        isAudioSelection = false
                    )
                }
                else-> {
                    Toast.makeText(this, "choosen method does not contain videos", Toast.LENGTH_SHORT).show()
                }
            }

        }

        //Button Image File Dialog
        btnImageDialog.setOnClickListener() {
            when(methodSpinner.selectedItem){
                "video from multiple images"-> {
                    //File Picker for multiple images
                    Common.selectFile(
                        this,
                        maxSelection = 5,
                        isImageSelection = true,
                        isAudioSelection = false
                    )
                }
                "video from image"-> {
                    //File Picker for a single image
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = true,
                        isAudioSelection = false
                    )
                }
                "video from video and image"-> {
                    //File Picker for a single image
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = true,
                        isAudioSelection = false
                    )
                }
                else-> {
                    Toast.makeText(this, "choosen method does not contain images", Toast.LENGTH_SHORT).show()
                }
            }

        }

        //Button Main Process
        btnCreateVideo.setOnClickListener()
        {

        }
    }

    //Catch the File Dialog and Codec Spinner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            //selectedFiles(mediaFiles, requestCode)

            val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
            if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                txtFilePath.text = (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
            }
        }
    }
}