/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.network.repository

import com.shohrab.newsapp.data.local.NewsDao
import com.shohrab.newsapp.data.model.NewsArticle
import com.shohrab.newsapp.data.model.NewsResponse
import com.shohrab.newsapp.network.api.ApiHelper
import com.shohrab.newsapp.utils.NetworkResult
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val remoteDataSource: ApiHelper,
    private val localDataSource: NewsDao
) {

    suspend fun getNews(countryCode: String, pageNumber: Int): NetworkResult<NewsResponse> {
        return try {
            val response = remoteDataSource.getNews(countryCode, pageNumber)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                NetworkResult.Success(result)
            } else {
                NetworkResult.Error("An error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error occurred ${e.localizedMessage}")
        }
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): NetworkResult<NewsResponse> {
        return try {
            val response = remoteDataSource.searchNews(searchQuery, pageNumber)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                NetworkResult.Success(result)
            } else {
                NetworkResult.Error("An error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error occurred ${e.localizedMessage}")
        }
    }

    suspend fun saveNews(news: NewsArticle) = localDataSource.upsert(news)

    fun getSavedNews() = localDataSource.getAllNews()

    suspend fun deleteNews(news: NewsArticle) = localDataSource.deleteNews(news)

    suspend fun deleteAllNews() = localDataSource.deleteAllNews()
}