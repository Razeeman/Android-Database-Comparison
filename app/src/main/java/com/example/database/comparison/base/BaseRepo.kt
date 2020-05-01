package com.example.database.comparison.base

import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataProvider

interface BaseRepo<T : BasePerson> {

    val name: String

    fun addAll(persons: List<T>)
    fun loadAll(): List<T>
    fun updateAll(persons: List<T>)
    fun deleteAll(persons: List<T>)

    fun clear()

    fun transformData(data: List<Person>): List<T>

    fun access(persons: List<T>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    fun change(persons: List<T>): List<T> {
        return persons.map { it.also { it.age = DataProvider.getInt() } }
    }
}