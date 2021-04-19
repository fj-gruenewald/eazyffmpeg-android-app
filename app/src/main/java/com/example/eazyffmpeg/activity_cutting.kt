package com.example.eazyffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class activity_cutting : AppCompatActivity() {

    //Variables
    private var startTimeString: String? = null
    private var endTimeString: String? = null
    private var maxTimeString: String? = null
    private var isInputVideoSelected: Boolean = false
    private var selectedVideoDurationInSecond = 0L
    var mediaFiles: List<MediaFile>? = null
    var retriever: MediaMetadataRetriever? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutting)

        //UI Variables
        val btnFileDialog = findViewById<ImageView>(R.id.btnFileDialog)
        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val btnCutting = findViewById<Button>(R.id.btnCutting)
        val btnFrom = findViewById<ImageView>(R.id.btnFrom)
        val btnTo = findViewById<ImageView>(R.id.btnTo)
        val txtFrom = findViewById<EditText>(R.id.txtFrom)
        val txtTo = findViewById<EditText>(R.id.txtTo)

        //File Dialog
        btnFileDialog.setOnClickListener()
        {
            //File Picker
            Common.selectFile(
                this,
                maxSelection = 1,
                isImageSelection = false,
                isAudioSelection = false
            )
        }

        //Get the start time for the cut
        btnFrom.setOnClickListener() {
            if (!TextUtils.isEmpty(maxTimeString) && !TextUtils.equals(maxTimeString, "00:00:00")) {
                selectTime(txtFrom, true)
            } else {
                Toast.makeText(this, "No Input Video selected", Toast.LENGTH_SHORT).show()
            }
        }

        //Get the end time for the cut
        btnTo.setOnClickListener() {
            if (!TextUtils.isEmpty(maxTimeString) && !TextUtils.equals(maxTimeString, "00:00:00")) {
                selectTime(txtTo, false)
            } else {
                Toast.makeText(this, "No Input Video selected", Toast.LENGTH_SHORT).show()
            }
        }

        //Do The Main Process
        btnCutting.setOnClickListener()
        {
            when {
                TextUtils.isEmpty(maxTimeString) -> {
                    Toast.makeText(this, "No Input Video selected", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(startTimeString) -> {
                    Toast.makeText(this, "No Input Video selected", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(endTimeString) -> {
                    Toast.makeText(this, "No Input Video selected", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (isValidation()) {
                        cuttingProcess()
                    } else {
                        Toast.makeText(this, "start and end time invalid", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    //
    @SuppressLint("NewApi")
    fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {

        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtTo = findViewById<EditText>(R.id.txtTo)

        if (requestCode == Common.VIDEO_FILE_REQUEST_CODE) {
            if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                maxTimeString = Common.stringForTime(mediaFiles[0].duration)
                //txtTo.text = "Selected video max time : $maxTimeString"
            } else {
                Toast.makeText(this, "No Video selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //
    @SuppressLint("SetTextI18n")
    private fun selectTime(tvTime: TextView, isStartTime: Boolean) {
        MyTimePickerDialog(this, { _, hourOfDay, minute, seconds ->
            val selectedTime = String.format("%02d", hourOfDay) + ":" + String.format(
                "%02d",
                minute
            ) + ":" + String.format("%02d", seconds)
            if (isSelectedTimeValid(selectedTime)) {
                tvTime.text = selectedTime
                if (isStartTime) {
                    startTimeString = selectedTime
                } else {
                    endTimeString = selectedTime
                }
            } else {
                Toast.makeText(this, "Time must be in Range", Toast.LENGTH_SHORT).show()
            }
        }, 0, 0, 0, true).show()
    }

    //
    private fun isSelectedTimeValid(selectedTime: String?): Boolean {
        var isBetween = false
        try {
            val time1: Date = SimpleDateFormat(
                Common.TIME_FORMAT,
                Locale.ENGLISH
            ).parse("00:00:00")
            val time2: Date =
                SimpleDateFormat(Common.TIME_FORMAT, Locale.ENGLISH).parse(maxTimeString)
            val sTime: Date =
                SimpleDateFormat(Common.TIME_FORMAT, Locale.ENGLISH).parse(selectedTime)
            if (time1.before(sTime) && time2.after(sTime)) {
                isBetween = true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isBetween
    }

    //
    private fun isValidation(): Boolean {
        var isBetween = false
        try {
            val time1: Date =
                SimpleDateFormat(Common.TIME_FORMAT, Locale.ENGLISH).parse(startTimeString)
            val time2: Date =
                SimpleDateFormat(Common.TIME_FORMAT, Locale.ENGLISH).parse(endTimeString)
            if (time1.before(time2)) {
                isBetween = true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isBetween
    }

    //The FFMPEG Process
    @SuppressLint("SetTextI18n")
    private fun cuttingProcess() {

        val txtFilePath = findViewById<EditText>(R.id.txtFilePath)
        val txtInfo = findViewById<TextView>(R.id.txtInfo)

        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query = FFmpegQueryExtension.cutVideo(
            txtFilePath.text.toString(),
            startTimeString,
            endTimeString,
            outputPath
        )
        CallBackOfQuery.callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                txtInfo.text = logMessage.text
            }

            override fun success() {
                txtInfo.text = String.format("Successfull: ", outputPath)
            }

            override fun cancel() {
            }

            override fun failed() {
            }
        })
    }

    //Get the File Path from the Picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            selectedFiles(mediaFiles, requestCode)

            val txtFilePath = findViewById<TextView>(R.id.txtFilePath)
            if (mediaFiles != null && (mediaFiles as ArrayList<MediaFile>).isNotEmpty()) {
                txtFilePath.text = (mediaFiles as ArrayList<MediaFile>)[0].path.toString()
            }
        }
    }
}