package com.example.database.comparison.base

import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataProvider

interface BaseRepo {

    val name: String

    fun addAll(persons: List<BasePerson>)
    fun loadAll(): List<BasePerson>
    fun updateAll(persons: List<BasePerson>)
    fun deleteAll(persons: List<BasePerson>)

    fun clear()

    fun transformData(data: List<BasePerson>): List<BasePerson>

    fun access(persons: List<BasePerson>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    fun change(persons: List<BasePerson>): List<BasePerson> {
        return persons.map { it.also { it.age = DataProvider.getInt() } }
    }
}