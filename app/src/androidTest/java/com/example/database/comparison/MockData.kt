package com.example.database.comparison

import com.example.database.comparison.dbms.greendao.model.PersonGreen
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.dbms.realm.model.PersonRealm
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer

class MockData {

    companion object {

        private const val FIRST_NAME1 = "first_name1"
        private const val FIRST_NAME2 = "first_name2"
        private const val FIRST_NAME3 = "first_name3"
        private const val SECOND_NAME1 = "second_name1"
        private const val SECOND_NAME2 = "second_name2"
        private const val SECOND_NAME3 = "second_name3"
        private const val AGE1 = 10
        private const val AGE2 = 20
        private const val AGE3 = 30

        private val person1 = Person(
            firstName = FIRST_NAME1,
            secondName = SECOND_NAME1,
            age = AGE1
        )
        private val person2 = Person(
            firstName = FIRST_NAME2,
            secondName = SECOND_NAME2,
            age = AGE2
        )
        private val person3 = Person(
            firstName = FIRST_NAME3,
            secondName = SECOND_NAME3,
            age = AGE3
        )

        private val persons = listOf(person1, person2, person3)

        fun getPersonsSQLite(): List<PersonSQLite> = DataTransformer
            .toPersonsSQLite(persons)

        fun getPersonsGreendao(): List<PersonGreen> = DataTransformer
            .toPersonsGreendao(persons)

        fun getPersonsObjectbox(): List<PersonObjectbox> = DataTransformer
            .toPersonsObjectbox(persons)

        fun getPersonsRealm(): List<PersonRealm> = DataTransformer
            .toPersonsRealm(persons)

        fun getPersonsRoom(): List<PersonRoom> = DataTransformer
            .toPersonsRoom(persons)
    }
}