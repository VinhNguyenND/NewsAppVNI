package com.example.myappnews.Data.Model.Article

 open class Article(
     val idArticle:String,
     val titleArticle:String,
     val linkArticle: String,
     val creator:List<String>,
     val content:String,
     val pubDate:String,
     val imageUrl: String,
     val sourceUrl:String,
     val sourceId: String,
     val country: String,
) {
}