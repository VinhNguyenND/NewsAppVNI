package com.example.myappnews.Data.Model.Dictionary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class License(
    val name: String,
    val url: String
) : Parcelable {

    // Hàm khởi tạo không có tham số
    constructor() : this(
        "", // Thay thế "" bằng giá trị mặc định nếu có
        "" // Thay thế "" bằng giá trị mặc định nếu có
    )
}