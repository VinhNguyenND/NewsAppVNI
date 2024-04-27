package com.example.myappnews.Data.Api.TextToSpeech.ObjectAudio

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

object FileWriter {
    private const val FILE_NAME = "audio_for_each_article"
    private lateinit var filePath: String

    fun initializeFile(inputStream: InputStream, sharedPreference: Shared_Preference) {
        if (sharedPreference.getFilePath() == null) {
            val tempFile = File.createTempFile(FILE_NAME, null)
            filePath = tempFile.absolutePath ?: ""
            sharedPreference.save(filePath)
        } else {
            filePath = sharedPreference.getFilePath()!!;
        }
        writeToFile(inputStream)
    }

    fun writeToFile(inputStream: InputStream) {
        val outputStream = FileOutputStream(filePath)
        try {
            val buffer = ByteArray(1000000)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } finally {
            outputStream.close()
        }
    }

    fun deleteAudioFromInternalStorage(name: String, context: Context) {
        val internalDir = context.filesDir
        val audioFile = File(internalDir, name)
        if (audioFile.exists()) {
            audioFile.delete()
        }
    }

    fun saveToInternal(
        internal: String,
        context: Context
    ): MutableLiveData<String> {
        val resultLiveData = MutableLiveData<String>()
        val cacheFile = getFileNameFromCache()?.let { File(context.cacheDir, it) }
        val internalFile = File(context.filesDir, internal)
        try {
            val inputStream = FileInputStream(cacheFile)
            val outputStream = FileOutputStream(internalFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            println("Copy cache to internal storage successfully.")
            resultLiveData.postValue(internalFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error copying cache to internal storage: ${e.message}")
            resultLiveData.postValue("fail")
        }


        return resultLiveData

    }

    fun getFileNameFromCache(): String? {
        if (filePath != null) {
            return filePath.substringAfterLast("/");
        }
        return null
    }

    fun getFilePath(): String {
        return filePath
    }


}