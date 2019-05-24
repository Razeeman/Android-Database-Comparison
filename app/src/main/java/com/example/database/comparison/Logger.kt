package com.example.database.comparison

import android.util.Log

class Logger {

    companion object {
        private const val TAG = "CUSTOM_TAG"
    }

    /**
     * Logs time with identification message.
     */
    fun log(identification: String, time: Long) {
        Log.d(TAG, "$identification, measured time: $time ms")
    }

}