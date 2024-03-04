package com.example.myappnews.Data.Api.Interface

import com.example.myappnews.Data.Model.Translate.Translate
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TranslateApi {
 @POST("api/v1/translator/text")
    @FormUrlEncoded
    @Headers(
        "content-type: application/x-www-form-urlencoded",
        "X-RapidAPI-Key: 76373408e2mshdd1b501acbcbf46p1b09c4jsn6300c60e03f5",
        "X-RapidAPI-Host: google-translate113.p.rapidapi.com"
    )
   suspend fun meanWord(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("text") text: String
    ): Response<Translate>
}