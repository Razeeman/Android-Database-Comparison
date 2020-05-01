package com.example.database.comparison.base

import com.example.database.comparison.model.BasePerson

interface BaseTest {

    fun run(runs: Int, data: List<BasePerson>)
}