package com.example.myappnews.Data.Local.Article.Down

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "ArticlesDown")
@Parcelize
data class ArticleDownEntity(
    @PrimaryKey
    @NonNull
    var idArticle: String,
    var titleArticle: String? = null,
    var linkArticle: String? = null,
    var creator: String? = null,
    var content: String? = null,
    var pubDate: String? = null,
    var imageUrl: String? = null,
    var sourceUrl: String? = null,
    var sourceId: String? = null,
    var country: String? = null,
    var field: String? = null,
    var sourceVoice:String?=null,
): Parcelable {

}