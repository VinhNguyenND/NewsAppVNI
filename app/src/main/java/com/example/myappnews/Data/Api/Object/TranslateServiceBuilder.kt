package com.example.myappnews.Data.Api.Object

import com.example.myappnews.Data.Api.Interface.TranslateApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TranslateServiceBuilder {
    const val BASE_URL="https://google-translate113.p.rapidapi.com/";
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val translate: TranslateApi by lazy {
        retrofitBuilder
            .build()
            .create(TranslateApi::class.java)
    }

}