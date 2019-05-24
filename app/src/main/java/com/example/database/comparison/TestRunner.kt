package com.example.database.comparison

class TestRunner(private val logger: Logger) {

    /**
     * Executes given code, measured its running time and log it.
     */
    fun run(identification: String, runs: Int = 1, func: () -> Unit) {
        val runTimes = LongArray(runs)

        for (i in 0 until runs) {
            // TODO System or SystemClock
            val startTime = System.currentTimeMillis()
            // Execute given code.
            func()
            runTimes[i] = System.currentTimeMillis() - startTime
        }

        val averageRunTime = runTimes.sum()/runs

        logger.log(identification, averageRunTime)
    }

}