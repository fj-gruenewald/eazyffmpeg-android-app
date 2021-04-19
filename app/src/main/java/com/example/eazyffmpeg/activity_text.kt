package com.example.eazyffmpeg

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.io.File

class activity_text : AppCompatActivity() {

    //
    private var context: Context? = null

    //Variables
    lateinit var video: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        this.context = this


        //Make the UI Variables
        val spinner = findViewById<Spinner>(R.id.codec_spinner)
        val codecs = resources.getStringArray(R.array.text_location)
        val bttn_addTextToVideo = findViewById<Button>(R.id.bttn_addTextToVideo)
        val openVideoInputFolderImageView = findViewById<ImageView>(R.id.openVideoInputFolderImageView)

        //openVideoInputFolderImageView when clicking on ImageView
        openVideoInputFolderImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select an Video file"), 111)
        })

        //bttn_addTextToVideo Click
        bttn_addTextToVideo.setOnClickListener(View.OnClickListener {

            Toast.makeText(this,"wololo",Toast.LENGTH_LONG).show();

        })


        //Check if Spinner is empty ==> give him some strings
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, codecs)
            spinner.adapter = adapter

            //When something gets selected ==> print what got selected
            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@activity_text,
                            getString(R.string.selected_item) + " " +
                                    "" + codecs[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
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