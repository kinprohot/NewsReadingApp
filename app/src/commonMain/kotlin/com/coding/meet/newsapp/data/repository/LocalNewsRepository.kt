package com.coding.meet.newsapp.data.repository

import com.coding.meet.newsapp.data.database.NewsDao
import com.coding.meet.newsapp.data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn

class LocalNewsRepository(
    private val newsDao: NewsDao
) {
    suspend fun upsertArticle(article: Article) {
        newsDao.upsert(article)
    }

    suspend fun deleteArticle(article: Article) {
        newsDao.delete(article)
    }

    suspend fun deleteAllBookmark() {
        newsDao.deleteAllBookmark()
    }

    fun getArticles() = newsDao.getArticles().flowOn(Dispatchers.IO)

    suspend fun getArticle(articleId: String): Article? {
        return newsDao.getArticle(articleId = articleId)
    }
}