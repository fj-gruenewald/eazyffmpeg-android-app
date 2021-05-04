package com.example.eazyffmpeg

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //UI Variables
        val btnDayNight = findViewById<ImageView>(R.id.btnDayNight)

        //Change theme on click
        btnDayNight.setOnClickListener(){

            val currentNightMode = (resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK)

            when(currentNightMode)
            {
                Configuration.UI_MODE_NIGHT_NO ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                Configuration.UI_MODE_NIGHT_YES ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
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