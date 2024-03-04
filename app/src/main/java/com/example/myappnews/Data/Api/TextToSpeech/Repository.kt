package com.example.myappnews.Data.Api.TextToSpeech

import android.media.AudioAttributes
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class Repository {
    private var mediaPlayer: MediaPlayer? = null
    private suspend fun getVoid(text:String){

    }

    public suspend fun callTextToSpeech(text: String,apiKey:String,apiHost:String){
        val client = OkHttpClient()
        val request =  Request.Builder()
            .url("https://text-to-speech27.p.rapidapi.com/speech?text=$text&lang=en-us")
            .get()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body
                    val inputStream = responseBody?.byteStream()
                    try {
                        //trong đây khi dữ liệu lấy về sẽ được trả về và lưu vào cache
                        Log.d("nhan ve",responseBody.toString())
                        Log.d("nhan ve",inputStream.toString())
                        if (inputStream != null) {
                            val tempFile = createTempFile(inputStream)
                            // Initialize MediaPlayer
                            mediaPlayer = MediaPlayer()

                            // Set data source
                            mediaPlayer?.setDataSource(tempFile.absolutePath)

                            // Prepare and start playback
                            GlobalScope.launch(Dispatchers.Main) {
                                mediaPlayer?.prepare()
                                mediaPlayer?.start()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        inputStream?.close()
                    }
                }
            }
        })
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun buildAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    }

    private fun createTempFile(inputStream: InputStream): File {
        val tempFile = File.createTempFile("audio_temp", null)
        tempFile.deleteOnExit()
        val outputStream = FileOutputStream(tempFile)

        try {
            val buffer = ByteArray(100000)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } finally {
            outputStream.close()
        }

        return tempFile
    }}