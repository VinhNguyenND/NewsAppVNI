package com.example.myappnews.Data.Model.Translate

data class Translate(
    val dict: List<Dict>,
    val source_language: String,
    val source_language_code: String,
    val trans: String,
    val trust_level: Float
)