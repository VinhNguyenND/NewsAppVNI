package com.example.myappnews.Data.Api.TextToSpeech.ObjectAudio

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileWriter {
    private const val FILE_NAME = "audio_for_each_article"
    private lateinit var filePath: String
    private  var tempFile:File?=null;

    fun initializeFile(inputStream: InputStream) {
      if (tempFile == null) {
            tempFile = File.createTempFile(FILE_NAME, null)
            tempFile?.deleteOnExit()
            filePath = tempFile?.absolutePath ?: ""
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

    fun getFilePath(): String {
        return filePath
    }
}