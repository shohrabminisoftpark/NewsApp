/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.shohrab.newsapp.R
import com.shohrab.newsapp.base.BaseActivity
import com.shohrab.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    val mainViewModel: MainViewModel by viewModels()

    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Today's News";
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun setBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun setupBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.feedFragment,
                R.id.favoriteFragment
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        super.onBackPressed();
    }
}