package com.example.eazyffmpeg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            FFmpeg.getInstance(this).loadBinary(object : FFmpegLoadBinaryResponseHandler {
                override fun onFailure() {
                    val toto = Toast.makeText(this@MainActivity," Failed ", Toast.LENGTH_SHORT)
                    toto.show()
                }

                override fun onSuccess() {
                    val toto = Toast.makeText(this@MainActivity," Sucess ", Toast.LENGTH_SHORT)
                    toto.show()
                }

                override fun onStart() {
                    val toto = Toast.makeText(this@MainActivity," Start ", Toast.LENGTH_SHORT)
                    toto.show()
                }

                override fun onFinish() {
                    val toto = Toast.makeText(this@MainActivity," Finish", Toast.LENGTH_SHORT)
                    toto.show()
                }
            })
        } catch (e: FFmpegNotSupportedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
        val intent = Intent(this, activity_converter::class.java)
        startActivity(intent)
    }
    fun videoTextAndTitleClick(view:View)
    {
        val intent = Intent(this, activity_text::class.java)
        startActivity(intent)
    }
    fun soundClick(view:View)
    {
        val intent = Intent(this, activity_sound::class.java)
        startActivity(intent)
    }
    fun settingsClick(view:View)
    {
        val intent = Intent(this, activity_settings::class.java)
        startActivity(intent)
    }

}