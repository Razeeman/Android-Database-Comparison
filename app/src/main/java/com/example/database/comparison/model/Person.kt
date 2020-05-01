package com.example.database.comparison.model

data class Person (
    override var id: Long = 0,
    override var firstName: String,
    override var secondName: String,
    override var age: Int
) : BasePerson