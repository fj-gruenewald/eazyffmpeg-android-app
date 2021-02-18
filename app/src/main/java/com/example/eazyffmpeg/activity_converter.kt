package com.example.eazyffmpeg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class activity_converter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        //Fill the Codec Spinner
        //Make the Variables
        val spinner = findViewById<Spinner>(R.id.codec_spinner)
        val codecs = resources.getStringArray(R.array.codecs)

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
}