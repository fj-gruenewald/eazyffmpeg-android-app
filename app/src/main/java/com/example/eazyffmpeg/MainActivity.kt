package com.example.eazyffmpeg

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    //OnClick Events for the Menu Items
    fun videoCreationClick(view: View) {
        val intent = Intent(this, activity_creation::class.java)
        startActivity(intent)
    }

    fun videoCuttingClick(view: View) {
        val intent = Intent(this, activity_cutting::class.java)
        startActivity(intent)
    }

    fun videoConvertClick(view: View) {
        val intent = Intent(this, activity_compress::class.java)
        startActivity(intent)
    }

    fun videoTextAndTitleClick(view: View) {
        val intent = Intent(this, activity_text::class.java)
        startActivity(intent)
    }

    fun soundClick(view: View) {
        val intent = Intent(this, activity_effects::class.java)
        startActivity(intent)
    }

    fun settingsClick(view: View) {
        val intent = Intent(this, activity_transform::class.java)
        startActivity(intent)
    }

}