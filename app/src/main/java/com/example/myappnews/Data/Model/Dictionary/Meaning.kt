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
        var antonyms: String = ""
        var definitions: String = "";
        var partOfSpeech: String = "";
        var synonyms: String = "";
        if (this.antonyms.size > 0) {
            antonyms = "\nantonyms: ";
            this.antonyms.forEachIndexed { index, doc ->
                if (index <= 2) {
                    antonyms += " " + doc;
                }
            }
        }
        if (this.definitions.size > 0) {
            definitions = "\ndefinitions: "
            this.definitions.forEachIndexed { index, doc ->
                if (index < 2) {
                    definitions += doc;
                }
            }
        }
        if (this.partOfSpeech.length > 0) {
            partOfSpeech = "\npartOfSpeech: " + this.partOfSpeech
        }
        if (this.synonyms.size > 0) {
            synonyms = "\nsynonyms: "
            this.synonyms.forEachIndexed { index, doc ->
                if (index < 2) {
                    synonyms += this.synonyms.toString()
                }
            }
        }
        return "$antonyms    $definitions   $partOfSpeech   $synonyms";
    }


}