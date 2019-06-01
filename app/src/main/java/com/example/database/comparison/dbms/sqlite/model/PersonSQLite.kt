package com.example.database.comparison.dbms.sqlite.model

data class PersonSQLite (
    var firstName: String,
    var secondName: String,
    var age: Int
) {

    var id: Long = 0

}