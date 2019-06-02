package com.example.database.comparison.util

class Runner(private val logger: Logger) {

    private var before: () -> Unit = {}
    private var beforeEach: () -> Unit = {}

    /**
     * Set up a function to run once before tests.
     */
    fun before(func: () -> Unit): Runner {
        before = func
        return this
    }

    /**
     * Set up a function to run before each test.
     */
    fun beforeEach(func: () -> Unit): Runner {
        beforeEach = func
        return this
    }

    /**
     * Executes given code, measures its running time and logs it.
     *
     * @param name   run identification to pass to logger.
     * @param runs   number of runs of code to measure execution time.
     * @param warmup number of warm-up runs before starting time measurements.
     * @param func   code to measure.
     */
    fun run(name: String, runs: Int = 10, warmup: Int = 10, func: () -> Unit) {
        val runTimes = LongArray(runs)
        var startTime: Long
        var finishTime: Long

        before()

        for (i in 0 until runs + warmup) {
            beforeEach()

            startTime = System.currentTimeMillis()

            func()

            finishTime = System.currentTimeMillis()

            if (i >= warmup) runTimes[i - warmup] = finishTime - startTime
        }

        before = {}
        beforeEach = {}

        val averageRunTime = runTimes.sum() / runs

        logger.log(name, averageRunTime)
    }

}