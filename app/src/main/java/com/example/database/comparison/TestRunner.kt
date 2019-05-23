package com.example.database.comparison

import android.util.Log

class TestRunner {

    /**
     * Executes given code, measured its running time and log it.
     */
    fun run(message: String, func: () -> Unit) {
        // TODO System or SystemClock
        val startTime = System.currentTimeMillis()

        // Execute given code.
        func()

        val testTime = System.currentTimeMillis() - startTime
        Log.d(message, "measured time $testTime ms")
    }

}