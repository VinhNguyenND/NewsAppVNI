package com.example.myappnews.Data.Api.Object

import com.example.myappnews.Data.Api.Interface.DicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DictionaryServiceBuilder {
    const val BASE_URL="https://api.dictionaryapi.dev/api/v2/entries/en/";
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val DicService: DicApi by lazy {
        retrofitBuilder
            .build()
            .create(DicApi::class.java)
    }
}