package com.example.myappnews.Data.Local.Dictionary.Entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "DictionaryItem",
    foreignKeys = [
        ForeignKey(
            entity = DictionaryFolder::class,
            parentColumns = ["idDictionaryFolder"],
            childColumns = ["idDictionaryFolder"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
@Parcelize
class DictionaryItem(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var idDictionaryItem: Int,
    var idDictionaryFolder: Int,
    var word:String,
    var phonetic:String,
    var mean:String,
    var audio:String,
    var wordMean: String
): Parcelable {

}