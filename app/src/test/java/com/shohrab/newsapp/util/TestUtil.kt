/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.util

import com.shohrab.newsapp.data.model.NewsArticle
import com.shohrab.newsapp.data.model.NewsResponse
import com.shohrab.newsapp.data.model.Source
import com.shohrab.newsapp.di.CoroutinesDispatcherProvider
import com.shohrab.newsapp.utils.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

object TestUtil {
    fun getFakeNewsArticleResponse(): NetworkResult<NewsResponse> {
        val articles = getFakeArticles()
        val newsResponse = NewsResponse(
            articles = articles, "200", 2
        )
        return NetworkResult.Success(newsResponse)
    }

    fun getFakeArticles(): MutableList<NewsArticle> {
        val articleList: MutableList<NewsArticle> = arrayListOf()
        val source1 = Source(
            id = 1, name = "BBC"
        )
        val article1 = NewsArticle(
            id = 1, author = "A", content = "ABC", description = "Desc1", publishedAt = "",
            source = source1, title = "Title1", url = "https://google.com", urlToImage = ""
        )
        val source2 = Source(
            id = 2, name = "CNN"
        )
        val article2 = NewsArticle(
            id = 2, author = "B", content = "DEF", description = "Desc2", publishedAt = "",
            source = source2, title = "Title2", url = "https://youtube.com", urlToImage = ""
        )

        articleList.add(article1)
        articleList.add(article2)
        return articleList
    }

    fun getFakeArticle(): NewsArticle {
        val source1 = Source(
            id = 1, name = "BBC"
        )
        val article1 = NewsArticle(
            id = 1, author = "A", content = "ABC", description = "Desc1", publishedAt = "",
            source = source1, title = "Title1", url = "https://google.com", urlToImage = ""
        )
        return article1
    }


    @ExperimentalCoroutinesApi
    fun provideFakeCoroutinesDispatcherProvider(
        dispatcher: TestCoroutineDispatcher?
    ): CoroutinesDispatcherProvider {
        val sharedTestCoroutineDispatcher = TestCoroutineDispatcher()
        return CoroutinesDispatcherProvider(
            dispatcher ?: sharedTestCoroutineDispatcher,
            dispatcher ?: sharedTestCoroutineDispatcher,
            dispatcher ?: sharedTestCoroutineDispatcher
        )
    }
}