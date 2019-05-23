package com.example.database.comparison

class TestRunner(val logger: Logger) {

    /**
     * Executes given code, measured its running time and log it.
     */
    fun run(identification: String, func: () -> Unit) {
        // TODO System or SystemClock
        val startTime = System.currentTimeMillis()

        // Execute given code.
        func()

        val finishTime = System.currentTimeMillis()
        logger.log(identification, startTime, finishTime)
    }

}