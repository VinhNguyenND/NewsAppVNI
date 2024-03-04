package com.example.myappnews.Data.Model.Dictionary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Phonetic(
    val audio: String,
    val license: License,
    val sourceUrl: String,
    val text: String
) : Parcelable {

    // Hàm khởi tạo không có tham số
    constructor() : this(
        "", // Thay thế "" bằng giá trị mặc định nếu có
        License(), // Thay thế License() bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        "" // Thay thế "" bằng giá trị mặc định nếu có
    )
}