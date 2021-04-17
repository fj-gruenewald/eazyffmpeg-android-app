package com.example.eazyffmpeg

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.eazyffmpeg.functions.ffmpegAudioOnVideo
import com.example.eazyffmpeg.functions.ffmpegCallback
import java.io.File

class activity_sound : AppCompatActivity() {

        //Tag and Context
        var context: Context? = null

        //Variables for FFmpeg
        lateinit var audio: File
        lateinit var video: File

        //Variables for the UI
        val openVideoInputFolderImageView = findViewById<ImageView>(R.id.openVideoInputFolderImageView)
        val openAudioInputFolderImageView = findViewById<ImageView>(R.id.openAudioInputFolderImageView)
        val bttn_addAudioToVideo = findViewById<Button>(R.id.bttn_addAudioToVideo)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        this.context = this

        //Ask for needed permissions
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@activity_sound, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 2222)
        } else if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@activity_sound, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 2222)
        }

        //openVideoInputFolderImageView when clicking on ImageView
        openVideoInputFolderImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select an Video file"), 111)
        })

        //openAudioInputFolderImageView when clicking on ImageView
        openAudioInputFolderImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select an Audio file"), 111)
        })

        //bttn_addAudioToVideo Click
        bttn_addAudioToVideo.setOnClickListener(View.OnClickListener {

            Toast.makeText(this,"Button clicked",Toast.LENGTH_LONG).show();

            //Kill previous running process
            //stopRunningProcess()

            //call the AudioVideoMerger
            // test if isRunning
            ffmpegAudioOnVideo.with(context!!)
                    .setAudioFile(audio) //Audio File
                    .setVideoFile(video) //Video File
                    .setOutputPath("PATH_TO_OUTPUT_VIDEO")
                    .setOutputFileName("merged_" + System.currentTimeMillis() + ".mp4")
                    .merge()

        })
    }

    //catch the openVideoInputFolderImageView Dialog Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val txtInput = findViewById<EditText>(R.id.txt_videoInputPath)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            val path = selectedFile?.lastPathSegment.toString().removePrefix("raw:")
            txtInput.setText(path)
        }
    }
}