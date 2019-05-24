package com.example.database.comparison

import android.util.Log

class Logger {

    companion object {
        private const val TAG = "CUSTOM_TAG"
    }

    /**
     * Logs time with identification message.
     */
    fun log(name: String, time: Long) {
        Log.d(TAG, "$name, measured time: $time ms")
    }

}