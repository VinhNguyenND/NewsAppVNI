package com.example.myappnews.Data.Model.Dictionary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String,
    val synonyms: List<String>
) : Parcelable {

    // Hàm khởi tạo không có tham số
    constructor() : this(
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        emptyList() // Thay thế emptyList() bằng giá trị mặc định nếu có
    )
}