package com.example.myappnews.Data.Local.Article

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull;
import com.example.myappnews.Data.Model.Article.NewsArticle


@Entity(tableName = "Articles")
data class ArticleEntity(
    @PrimaryKey
    @NonNull
    var idArticle:String,
    var titleArticle:String?=null,
    var linkArticle: String?=null,
    var creator:String?=null,
    var content:String?=null,
    var pubDate: String?=null,
    var imageUrl: String?=null,
    var sourceUrl:String?=null,
    var sourceId: String?=null,
    var country: String?=null,
    var field:String?=null,
)
