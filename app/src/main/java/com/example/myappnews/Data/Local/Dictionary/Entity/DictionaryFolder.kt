package com.example.myappnews.Data.Local.Dictionary.Entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Update
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "DictionaryFolder")
@Parcelize
class DictionaryFolder(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var idDictionaryFolder: Int,
    var nameDictionaryFolder:String,
    var timeUpdate: Long,
    var numDictionary:Int,
): Parcelable {

}