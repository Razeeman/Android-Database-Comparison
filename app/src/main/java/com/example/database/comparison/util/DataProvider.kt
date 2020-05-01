package com.example.database.comparison.util

import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.model.Person
import java.util.*
import kotlin.collections.ArrayList

/**
 * Provides randomly generated data for tests.
 */
class DataProvider {

    companion object {
        private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
        private const val DIGITS = "0123456789"
        private const val SYMBOLS = UPPER + LOWER + DIGITS

        private val random = Random()

        /**
         * Provides random integer.
         */
        fun getInt(bound: Int = 1000): Int {
            return random.nextInt(bound)
        }

        /**
         * Provides random alphanumeric string.
         */
        fun getString(len: Int = 10): String {
            val sb = StringBuilder(len)
            for (i in 0 until len) {
                sb.append(SYMBOLS[random.nextInt(SYMBOLS.length)])
            }
            return sb.toString()
        }
    }

    /**
     * Provides random person.
     */
    private fun getPerson(): BasePerson {
        return Person(
            firstName = getString(),
            secondName = getString(),
            age = getInt()
        )
    }

    /**
     * Provides list of number of random persons.
     */
    fun getPersons(num: Int): List<BasePerson> {
        val list = ArrayList<BasePerson>()

        for (i in 0 until num) {
            list.add(getPerson())
        }

        return list
    }

}