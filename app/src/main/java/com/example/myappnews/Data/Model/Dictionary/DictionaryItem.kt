package com.example.myappnews.Data.Model.Dictionary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DictionaryItem(
    var license: License,
    val meanings: List<Meaning>,
    var phonetic: String,
    val phonetics: List<Phonetic>,
    val sourceUrls: List<String>,
    var word: String,
    var wordMean: String,
) : Parcelable {
    constructor() : this(
        License(), // Thay thế License() bằng giá trị mặc định nếu có
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        ""
    )

}