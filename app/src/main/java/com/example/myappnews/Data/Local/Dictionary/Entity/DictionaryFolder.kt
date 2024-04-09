package com.example.myappnews.Data.Local.Dictionary.Entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Update

@Entity(tableName = "DictionaryFolder")
class DictionaryFolder(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var idDictionaryFolder: Int,
    var nameDictionaryFolder:String,
    var timeUpdate: Long,
    var numDictionary:Int,
)