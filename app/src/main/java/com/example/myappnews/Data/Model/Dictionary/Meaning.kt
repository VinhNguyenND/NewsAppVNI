package com.example.myappnews.Data.Model.Dictionary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meaning(
    var antonyms: List<String>,
    val definitions: List<Definition>,
    var partOfSpeech: String,
    var synonyms: List<String>
) : Parcelable {

    // Hàm khởi tạo không có tham số
    constructor() : this(
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        emptyList(), // Thay thế emptyList() bằng giá trị mặc định nếu có
        "", // Thay thế "" bằng giá trị mặc định nếu có
        emptyList() // Thay thế emptyList() bằng giá trị mặc định nếu có
    )

    override fun toString(): String {
        var antonyms: String = "null"
        var definitions: String = "null";
        var partOfSpeech: String = "null";
        var synonyms: String = "null";
        if (this.antonyms.size > 0) {
            antonyms= this.antonyms.toString()
        }
        if (this.definitions.size > 0) {
           definitions=this.definitions.toString()
        }
        if (this.partOfSpeech.length>0) {
          partOfSpeech=this.partOfSpeech
        }
        if (this.synonyms.size > 0) {
         synonyms=this.synonyms.toString()
        }
        return "Meanings:\n +antonyms:$antonyms; \n  +definitions: $definitions; \n  +partOfSpeech:$partOfSpeech; \n  +synonyms:$synonyms \n"
    }




}