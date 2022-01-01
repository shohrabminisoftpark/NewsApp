/*
 * *
 *  * Created by Shohrab hossen on 1/1/22, 2:48 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.shohrab.newsapp.util

import androidx.test.platform.app.InstrumentationRegistry
import com.shohrab.newsapp.NewsApp
import java.io.IOException
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext as NewsApp).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}