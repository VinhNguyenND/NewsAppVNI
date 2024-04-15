package com.example.myappnews.Data.Model.Source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source (
   var SourceName: String?= null,
   var choose:Boolean=false,
): Parcelable {

}