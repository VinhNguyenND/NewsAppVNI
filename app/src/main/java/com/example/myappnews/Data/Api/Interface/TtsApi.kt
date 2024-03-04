package com.example.myappnews.Data.Api.Interface

import com.example.myappnews.Data.Model.Audio.Audio
import retrofit2.Response

interface TtsApi {
    suspend fun getAudio():Response<Audio>
}