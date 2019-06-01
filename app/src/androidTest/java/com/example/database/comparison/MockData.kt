package com.example.database.comparison

import com.example.database.comparison.dbms.sqlite.model.PersonSQLite

class MockData {

    companion object {

        const val FIRST_NAME1 = "first_name1"
        const val FIRST_NAME2 = "first_name2"
        const val FIRST_NAME3 = "first_name3"
        const val SECOND_NAME1 = "second_name1"
        const val SECOND_NAME2 = "second_name2"
        const val SECOND_NAME3 = "second_name3"
        const val AGE1 = 10
        const val AGE2 = 20
        const val AGE3 = 30

        val person1 = PersonSQLite(FIRST_NAME1, SECOND_NAME1, AGE1)
        val person2 = PersonSQLite(FIRST_NAME2, SECOND_NAME2, AGE2)
        val person3 = PersonSQLite(FIRST_NAME3, SECOND_NAME3, AGE3)

        fun getPersonsSQLite(): List<PersonSQLite> =  listOf(person1, person2, person3)

    }

}