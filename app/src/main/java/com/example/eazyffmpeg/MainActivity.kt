package com.example.eazyffmpeg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    //OnClick Events for the Menu Items
    fun videoCreationClick(view:View)
    {
        val intent = Intent(this, activity_creation::class.java)
        startActivity(intent)
    }
    fun videoCuttingClick(view:View)
    {
        val intent = Intent(this, activity_cutting::class.java)
        startActivity(intent)
    }
    fun videoConvertClick(view:View)
    {
        val intent = Intent(this, activity_compress::class.java)
        startActivity(intent)
    }
    fun videoTextAndTitleClick(view:View)
    {
        val intent = Intent(this, activity_text::class.java)
        startActivity(intent)
    }
    fun soundClick(view:View)
    {
        val intent = Intent(this, activity_effects::class.java)
        startActivity(intent)
    }
    fun settingsClick(view:View)
    {
        val intent = Intent(this, activity_settings::class.java)
        startActivity(intent)
    }

}