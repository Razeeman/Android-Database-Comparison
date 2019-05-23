package com.example.database.comparison

import android.util.Log

class Logger {

    /**
     * Logs time with identification message.
     */
    fun log(identification: String, startTime: Long, finishTime: Long) {
        Log.d(identification, "measured time: " +  (finishTime - startTime) + "ms")
    }

}