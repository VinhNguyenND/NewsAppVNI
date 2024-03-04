package com.example.myappnews.Data.Model.Translate

data class Dict(
    val base_form: String,
    val entry: List<Entry>,
    val pos: String,
    val pos_enum: Int,
    val terms: List<String>
)