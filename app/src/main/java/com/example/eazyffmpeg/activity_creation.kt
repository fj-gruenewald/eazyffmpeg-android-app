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
import java.util.*
import java.util.concurrent.CompletableFuture

class activity_creation : AppCompatActivity() {

    //Variables
    private var isInputVideoSelected: Boolean = false
    private var isInputImageSelected: Boolean = false
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null
    var height: Int? = 0
    var width: Int? = 0

    var actionToPerform: String = getString(R.string.placeholderString)
    //action to perform
    // 1 --> video from image
    // 2 --> video from videos
    // 3 --> video from images
    // 4 --> video from video and image

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
        btnFileDialog.setOnClickListener {
            when (methodSpinner.selectedItem) {
                "video from videos" -> {
                    //File Picker for multiple videos
                    Common.selectFile(
                        this,
                        maxSelection = 5,
                        isImageSelection = false,
                        isAudioSelection = false
                    )

                    //set the action variable
                    actionToPerform = "2"
                }
                "video from video and image" -> {
                    //File Picker for a single video
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = false,
                        isAudioSelection = false
                    )

                    //set the action variable
                    actionToPerform = "4"
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.actionWithoutVideo),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        //Button Image File Dialog
        btnImageDialog.setOnClickListener {
            when (methodSpinner.selectedItem) {
                "video from multiple images" -> {
                    //File Picker for multiple images
                    Common.selectFile(
                        this,
                        maxSelection = 5,
                        isImageSelection = true,
                        isAudioSelection = false
                    )

                    //set the action variable
                    actionToPerform = "3"
                }
                "video from image" -> {
                    //File Picker for a single image
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = true,
                        isAudioSelection = false
                    )

                    //set the action variable
                    actionToPerform = "1"
                }
                "video from video and image" -> {
                    //File Picker for a single image
                    Common.selectFile(
                        this,
                        maxSelection = 1,
                        isImageSelection = true,
                        isAudioSelection = false
                    )

                    //set the action variable
                    actionToPerform = "4"
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.actionWithoutImage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        //Button Main Process
        btnCreateVideo.setOnClickListener()
        {
            when (methodSpinner.selectedItem) {
                "video from image" -> {
                    FFMPEG_VideoFromImage()
                }
                "video from videos" -> {
                    FFMPEG_CombineVideos()
                }
                "video from multiple images" -> {
                    FFMPEG_CombineImages()
                }
                "video from video and image" -> {
                    FFMPEG_CombineImageAndVideo()
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.creationMethodError),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    //FFMPEG PROCESS - Video from Image
    private fun FFMPEG_VideoFromImage() {
        val txtImageFilePath = findViewById<EditText>(R.id.txtImageFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val size: ISize = SizeOfImage(txtImageFilePath.text.toString())
        val query = FFmpegQueryExtension.imageToVideo(
            txtImageFilePath.text.toString(),
            outputPath,
            3,
            size.width(),
            size.height()
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

    //Get the Image File for making the Video
    @SuppressLint("NewApi")
    fun selectedImageFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        if (requestCode == Common.IMAGE_FILE_REQUEST_CODE) {
            if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                isInputImageSelected = true
            } else {
                isInputImageSelected = false
                Toast.makeText(this, getString(R.string.emptyImageSelection), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //FFMPEG PROCESS - Video from Videos
    private fun FFMPEG_CombineVideos() {
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val pathsList = ArrayList<Paths>()
        mediaFiles?.let {
            for (element in it) {
                val paths = Paths()
                paths.filePath = element.path
                paths.isImageFile = false
                pathsList.add(paths)
            }

            val query = FFmpegQueryExtension.combineVideos(
                pathsList,
                width,
                height,
                outputPath
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
    }

    //Get the Video Files to combine them into a new video
    @SuppressLint("NewApi", "SetTextI18n")
    fun selectedCombineVideoFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)

        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    val size: Int = mediaFiles.size
                    txtFilePath.text =
                        "$size" + (if (size == 1) " Video " else " Videos ") + "selected"
                    isInputVideoSelected = true
                    CompletableFuture.runAsync {
                        retriever = MediaMetadataRetriever()
                        retriever?.setDataSource(txtFilePath.text.toString())
                        val bit = retriever?.frameAtTime
                        if (bit != null) {
                            width = bit.width
                            height = bit.height
                        }
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


    //FFMPEG PROCESS - Video from multiple Images
    private fun FFMPEG_CombineImages() {
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val pathsList = ArrayList<Paths>()
        mediaFiles?.let {
            for (element in it) {
                val paths = Paths()
                paths.filePath = element.path
                paths.isImageFile = true
                pathsList.add(paths)
            }

            val query =
                FFmpegQueryExtension.combineImagesAndVideos(pathsList, 1280, 720, "3", outputPath)

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
    }

    //Get multiple Images to Combine
    @SuppressLint("NewApi", "SetTextI18n")
    fun selectedCombineImageFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)

        when (requestCode) {
            Common.IMAGE_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    val size: Int = mediaFiles.size
                    txtImageFilePath.text =
                        "$size" + (if (size == 1) " Image " else " Images ") + "selected"
                    isInputImageSelected = true
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.emptyImageSelection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //FFMPEG PROCESS - Video from Video and Image
    fun FFMPEG_CombineImageAndVideo() {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val paths = ArrayList<Paths>()

        val videoPaths1 = Paths()
        videoPaths1.filePath = txtImageFilePath.text.toString()
        videoPaths1.isImageFile = true

        val videoPaths2 = Paths()
        videoPaths2.filePath = txtFilePath.text.toString()
        videoPaths2.isImageFile = false

        paths.add(videoPaths1)
        paths.add(videoPaths2)

        val query = FFmpegQueryExtension.combineImagesAndVideos(
            paths,
            width,
            height,
            "3",
            outputPath
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

    //Get the Image and the Video file for combining
    @SuppressLint("NewApi")
    fun selectedImageAndVideoFiles(mediaFiles: List<MediaFile>?, fileRequestCode: Int) {
        val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
        val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)

        when (fileRequestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    txtFilePath.text = mediaFiles[0].path
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
            +
            Common.IMAGE_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    txtImageFilePath.text = mediaFiles[0].path
                    isInputImageSelected = true
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.emptyImageSelection),
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

            when (actionToPerform) {
                //Video from Image
                "1" -> {
                    //do the right select file method
                    selectedImageFiles(mediaFiles, requestCode)

                    //write to txt
                    val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)
                    if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                        txtImageFilePath.text =
                            (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
                    }
                }
                //Video from Videos
                "2" -> {
                    //do the right select file method
                    selectedCombineVideoFiles(mediaFiles, requestCode)

                    //write to txt
                    val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
                    if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                        txtFilePath.text = (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
                    }
                }
                //Video from m Images
                "3" -> {
                    //do the right select file method
                    selectedCombineImageFiles(mediaFiles, requestCode)

                    //write to txt
                    val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)
                    if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                        txtImageFilePath.text =
                            (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
                    }
                }
                //Video from Video and Image
                "4" -> {
                    //do the right select file method
                    selectedImageAndVideoFiles(mediaFiles, requestCode)

                    //write to txt
                    val txtImageFilePath = findViewById<TextView>(R.id.txtImageFilePath)
                    if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                        txtImageFilePath.text =
                            (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
                    }
                }
            }
        }
    }
}