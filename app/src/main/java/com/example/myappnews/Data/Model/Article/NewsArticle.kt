package com.example.myappnews.Data.Model.Article

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class NewsArticle(
     val idArticle:String?=null,
     val titleArticle:String?=null,
     val linkArticle: String?=null,
     val creator:String?=null,
     val content:String?=null,
     val pubDate: Date?=null,
     val imageUrl: String?=null,
     val sourceUrl:String?=null,
     val sourceId: String?=null,
     val country: String?=null,
     val field:String?=null,
) : Parcelable {

}
