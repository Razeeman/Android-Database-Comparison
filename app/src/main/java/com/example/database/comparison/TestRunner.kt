package com.example.database.comparison

class TestRunner(private val logger: Logger) {

    private var before: () -> Unit = {}

    /**
     * Set up function to run before tests.
     */
    fun before(func: () -> Unit): TestRunner {
        before = func
        return this
    }

    /**
     * Executes given code, measured its running time and log it.
     */
    fun run(identification: String, runs: Int = 1, func: () -> Unit) {
        val runTimes = LongArray(runs)

        for (i in 0 until runs) {
            // Execute preparation code.
            before()

            // TODO System or SystemClock
            val startTime = System.currentTimeMillis()
            // Execute tested code.
            func()
            runTimes[i] = System.currentTimeMillis() - startTime
        }

        val averageRunTime = runTimes.sum() / runs

        logger.log(identification, averageRunTime)
    }

}