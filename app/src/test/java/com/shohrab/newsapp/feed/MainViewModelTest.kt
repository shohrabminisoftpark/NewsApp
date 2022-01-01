/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.shohrab.newsapp.data.model.NewsResponse
import com.shohrab.newsapp.network.api.NewsApi
import com.shohrab.newsapp.network.repository.NewsRepository
import com.shohrab.newsapp.ui.main.MainViewModel
import com.shohrab.newsapp.util.MainCoroutineRule
import com.shohrab.newsapp.util.TestUtil
import com.shohrab.newsapp.util.TestUtil.provideFakeCoroutinesDispatcherProvider
import com.shohrab.newsapp.util.runBlockingTest
import com.shohrab.newsapp.utils.Constants.Companion.CountryCode
import com.shohrab.newsapp.utils.NetworkHelper
import com.shohrab.newsapp.utils.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var newsApi: NewsApi

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var newsRepository: NewsRepository

    private val testDispatcher = coroutineRule.testDispatcher

    @Mock
    private lateinit var responseObserver: Observer<NetworkResult<NewsResponse>>
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(
            repository = newsRepository,
            networkHelper = networkHelper,
            coroutinesDispatcherProvider = provideFakeCoroutinesDispatcherProvider(testDispatcher)
        )
    }

    @Test
    fun `when calling for results then return loading`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            viewModel.newsResponse.observeForever(responseObserver)
            whenever(newsRepository.getNews(CountryCode, 1))
                .thenReturn(NetworkResult.Loading())

            //When
            viewModel.fetchNews(CountryCode)

            //Then
            assertThat(viewModel.newsResponse.value).isNotNull()
            assertThat(viewModel.newsResponse.value?.data).isNull()
            assertThat(viewModel.newsResponse.value?.message).isNull()
        }
    }

    @Test
    fun `test if feed is loaded with articles`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)

            viewModel.newsResponse.observeForever(responseObserver)
            // Stub repository with fake favorites
            whenever(newsRepository.getNews(CountryCode, 1))
                .thenAnswer { (TestUtil.getFakeNewsArticleResponse()) }

            //When
            viewModel.fetchNews(CountryCode)

            //then
            assertThat(viewModel.newsResponse.value).isNotNull()
            val articles = viewModel.newsResponse.value?.data?.articles
            assertThat(articles?.isNotEmpty())
            // compare the response with fake list
            assertThat(articles).hasSize(TestUtil.getFakeArticles().size)
            // compare the data and also order
            assertThat(articles).containsExactlyElementsIn(
                TestUtil.getFakeArticles()
            ).inOrder()
        }
    }

    @Test
    fun `test for failure`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            // Stub repository with fake favorites
            whenever(newsRepository.getNews(CountryCode, 1))
                .thenAnswer { NetworkResult.Error("Error occurred", null) }

            //When
            viewModel.fetchNews(CountryCode)

            //then
            val response = viewModel.newsResponse.value
            assertThat(response?.message).isNotNull()
            assertThat(response?.message).isEqualTo("Error occurred")
        }
    }

    @Test
    fun `test if search is loaded with search response`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            viewModel.searchNewsResponse.observeForever(responseObserver)
            // Stub repository with fake favorites
            whenever(newsRepository.searchNews(CountryCode, 1))
                .thenAnswer { (TestUtil.getFakeNewsArticleResponse()) }

            //When
            viewModel.searchNews(CountryCode)

            //then
            assertThat(viewModel.searchNewsResponse.value).isNotNull()
            val articles = viewModel.searchNewsResponse.value?.data?.articles
            assertThat(articles?.isNotEmpty())
            // compare the response with fake list
            assertThat(articles).hasSize(TestUtil.getFakeArticles().size)
            // compare the data and also order
            assertThat(articles).containsExactlyElementsIn(
                TestUtil.getFakeArticles()
            ).inOrder()
        }
    }

    @Test
    fun `test format date with T`() {
        val result = viewModel.formatDate("2021-09-29T13:01:31Z")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Sep 29, 2021 01:01 PM")
    }

    @Test
    fun `test format date without T`() {
        val result = viewModel.formatDate("2021-09-29 3:01:31 PM")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("2021-09-29 3:01:31 PM")
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
        viewModel.newsResponse.removeObserver(responseObserver)
        viewModel.searchNewsResponse.removeObserver(responseObserver)
    }
}