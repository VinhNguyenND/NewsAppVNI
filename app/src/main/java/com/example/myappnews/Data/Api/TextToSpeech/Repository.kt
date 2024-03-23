package com.example.myappnews.Data.Api.TextToSpeech

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Api.TextToSpeech.ObjectAudio.FileWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class Repository {
    private val _TextToSpechApi = MutableLiveData<String>()

    val TextToSpechApi: LiveData<String>
        get() = _TextToSpechApi

    companion object {
        private var instance: Repository? = null
        fun getInstance()=instance ?: synchronized(this){
             instance ?: Repository().also {
                instance = it
            }
        }

    }

    public suspend fun callTextToSpeech(text: String, apiKey: String, apiHost: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
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
                        if (inputStream != null) {
                             FileWriter.initializeFile(inputStream)
                            _TextToSpechApi.postValue(FileWriter.getFilePath())
                        } else {
                            _TextToSpechApi.postValue("");
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

//    private fun createTempFile(inputStream: InputStream): File {
//        val tempFile = File.createTempFile("audio_temp", null)
//        tempFile.deleteOnExit()
//        val outputStream = FileOutputStream(tempFile)
//        try {
//            val buffer = ByteArray(1000000)
//            var bytesRead: Int
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
//        } finally {
//            outputStream.close()
//        }
//        return tempFile
//    }


}
