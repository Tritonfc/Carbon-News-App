package com.tobe.carbonnewsapp.data.repository

import com.tobe.carbonnewsapp.data.localDataSource.ArticleDao
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.data.remoteDataSource.api.NewsApi
import javax.inject.Inject

class NewsRepository  @Inject constructor(private val newsApi: NewsApi, private val articleDao: ArticleDao) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchForNews(searchQuery, pageNumber)

    suspend fun insertNewsArticle(article: Article){
        articleDao.insert(article)
    }

    suspend fun fetchOfflineArticles() = articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article){
        articleDao.delete(article)
    }
}