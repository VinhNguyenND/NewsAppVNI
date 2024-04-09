package com.example.myappnews.Data.Local.Dictionary.Entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
class DictionaryItem(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var idDictionaryItem: Int,
    var idDictionaryFolder: Int,
    var word:String,
    var phonetic:String,
    var mean:String,
)