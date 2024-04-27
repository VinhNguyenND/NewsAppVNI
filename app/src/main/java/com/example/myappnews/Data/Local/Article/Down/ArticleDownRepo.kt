package com.example.myappnews.Data.Local.Article.Down

import androidx.lifecycle.LiveData

class ArticleDownRepo(private val articleDown: ArticleLDownDAO) {
    val readAllData: LiveData<List<ArticleDownEntity>> = articleDown.getAllDownArticle()

    suspend fun addArticle(articleEntity: ArticleDownEntity): Boolean {
        val result = articleDown.DownArticle(articleEntity)
        return result > 0;
    }

    suspend fun deleteDownArticle(id: String) {
        articleDown.deleteDownArticle(id)
    }

    suspend fun deleteAllArticle() {
        articleDown.disDownAllArticles();
    }

}