package com.example.database.comparison

class TestRunner(private val logger: Logger) {

    private var before: () -> Unit = {}
    private var beforeEach: () -> Unit = {}

    /**
     * Set up a function to run once before tests.
     */
    fun before(func: () -> Unit): TestRunner {
        before = func
        return this
    }

    /**
     * Set up a function to run before each test.
     */
    fun beforeEach(func: () -> Unit): TestRunner {
        beforeEach = func
        return this
    }

    /**
     * Executes given code, measured its running time and log it.
     */
    fun run(name: String, runs: Int = 1, func: () -> Unit) {
        val runTimes = LongArray(runs)

        before()

        for (i in 0 until runs) {
            // Execute preparation code.
            beforeEach()

            // TODO System or SystemClock
            val startTime = System.currentTimeMillis()
            // Execute tested code.
            func()
            runTimes[i] = System.currentTimeMillis() - startTime
        }

        before = {}
        beforeEach = {}

        val averageRunTime = runTimes.sum() / runs

        logger.log(name, averageRunTime)
    }

}