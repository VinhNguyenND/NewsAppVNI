package com.example.myappnews.Data.Local.Article.History

import androidx.lifecycle.LiveData

class ArticleRepository(private val articleDAO: ArticleDAO) {
    val readAllData: LiveData<List<ArticleEntity>> = articleDAO.getAllArticle()

    suspend fun addArticle(articleEntity: ArticleEntity) {
        articleDAO.insertArticle(articleEntity)
    }

    suspend fun deleteAllArticle() {
        articleDAO.deleteAllArticles();
    }
}