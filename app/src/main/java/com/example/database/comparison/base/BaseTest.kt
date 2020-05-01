package com.example.database.comparison.base

import com.example.database.comparison.model.Person

interface BaseTest {

    fun run(runs: Int, data: List<Person>)
}