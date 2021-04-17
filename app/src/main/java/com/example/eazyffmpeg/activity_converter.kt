package com.example.eazyffmpeg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eazyffmpeg.functions.ffmpegVideoResize

class activity_converter : AppCompatActivity() {

    //Tag and Context
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        this.context = this

        //Fill the Codec Spinner
        //Make the Variables
        val spinner = findViewById<Spinner>(R.id.codec_spinner)
        val codecs = resources.getStringArray(R.array.codecs)
        val openFolderImageView = findViewById<ImageView>(R.id.openFolderImageView)
        val bttn_convert = findViewById<Button>(R.id.bttn_convert)

        //openFolderBrowser when clicking on ImageView
        openFolderImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        })

        //bttn_convert Click
        bttn_convert.setOnClickListener(View.OnClickListener {

            Toast.makeText(this,"Button clicked",Toast.LENGTH_LONG).show();
            ffmpegVideoResize.with(context!!)
                    .setFile(video2) //Video File
                    .setSize("320:480") //320 X 480
                    .setOutputPath("PATH_TO_OUTPUT_VIDEO")
                    .setOutputFileName("resized_" + System.currentTimeMillis() + ".mp4")
                    .setCallback(this@activity_converter)
                    .resize()
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
                    Toast.makeText(this@activity_converter,
                            getString(R.string.selected_item) + " " +
                                    "" + codecs[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    //catch the file Dialog Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val txtInput = findViewById<EditText>(R.id.txt_InputPath)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            val path = selectedFile?.lastPathSegment.toString().removePrefix("raw:")
            txtInput.setText(path)
        }
    }
}