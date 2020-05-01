package com.example.database.comparison.util

import com.example.database.comparison.dbms.greendao.model.PersonGreen
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.dbms.realm.model.PersonRealm
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person

class DataTransformer private constructor() {

    companion object {

        private fun toPersonSQLite(person: Person): PersonSQLite {
            return PersonSQLite(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsSQLite(persons: List<Person>): List<PersonSQLite> {
            return persons.map(::toPersonSQLite)
        }

        private fun toPersonGreendao(person: Person): PersonGreen {
            return PersonGreen(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsGreendao(persons: List<Person>): List<PersonGreen> {
            return persons.map(::toPersonGreendao)
        }

        private fun toPersonObjectbox(person: Person): PersonObjectbox {
            return PersonObjectbox(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsObjectbox(persons: List<Person>): List<PersonObjectbox> {
            return persons.map(::toPersonObjectbox)
        }

        private fun toPersonRoom(person: Person): PersonRoom {
            return PersonRoom(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsRoom(persons: List<Person>): List<PersonRoom> {
            return persons.map(::toPersonRoom)
        }

        private fun toPersonRealm(person: Person): PersonRealm {
            return PersonRealm(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsRealm(persons: List<Person>): List<PersonRealm> {
            return persons.map {
                toPersonRealm(it).also { personRealm ->
                    personRealm.id = PersonRealm.objectCounter
                }
            }
        }
    }
}