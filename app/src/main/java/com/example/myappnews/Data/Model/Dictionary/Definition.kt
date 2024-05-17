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

    override fun toString(): String {
        var antonyms: String = "";
        var definition: String = "";
        var example: String = "";
        var synonyms: String = "";
        if (this.antonyms.size > 0) {
            for (doc in this.antonyms) {
                antonyms += "\n" + doc
            }
        }
        if (this.definition.length > 0) {
            definition = "\ndefinition: " + this.definition
        }
        if (this.synonyms.size > 0) {
            for (doc in this.synonyms) {
                synonyms += " " + doc
            }
        }
        if (this.example.length > 0) {
            example = "\nexample: " + this.example
        }
        return " $antonyms  $definition  $example  $synonyms "
    }

}