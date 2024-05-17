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
            for (doc in this.antonyms) {
                antonyms +=" " +doc;
            }
        }
        if (this.definitions.size > 0) {
            definitions = "\ndefinitions: "
            for (doc in this.definitions) {
                definitions += doc;
            }
        }
        if (this.partOfSpeech.length > 0) {
            partOfSpeech = "\npartOfSpeech: " + this.partOfSpeech
        }
        if (this.synonyms.size > 0) {
            synonyms = "\nsynonyms: " + this.synonyms.toString()
        }
        return "Meanings:$antonyms    $definitions   $partOfSpeech   $synonyms";
    }


}