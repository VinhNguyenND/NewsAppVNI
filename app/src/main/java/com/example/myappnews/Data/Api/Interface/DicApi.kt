package com.example.myappnews.Data.Api.Interface

import com.example.myappnews.Data.Model.Dictionary.DictionaryItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DicApi {
  @GET("{Word}")
  suspend fun listWord(@Path("Word")  word:String):Response<List<DictionaryItem>>;
}