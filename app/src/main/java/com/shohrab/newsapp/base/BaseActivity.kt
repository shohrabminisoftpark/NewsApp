/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = this.setBinding()
        setContentView(binding.root)

        //ready view to use
        onViewReady(savedInstanceState)
    }

    abstract fun setBinding(): T

    @CallSuper
    protected open fun onViewReady(savedInstanceState: Bundle?) {
        // use this method in child activity
    }


}