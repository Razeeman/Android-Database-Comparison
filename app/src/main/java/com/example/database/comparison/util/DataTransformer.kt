package com.example.database.comparison.util

import com.example.database.comparison.dbms.greendao.model.PersonGreen
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.dbms.realm.model.PersonRealm
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.BasePerson

class DataTransformer private constructor() {

    companion object {

        private fun toPersonSQLite(person: BasePerson): PersonSQLite {
            return PersonSQLite(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsSQLite(persons: List<BasePerson>): List<PersonSQLite> {
            return persons.map(::toPersonSQLite)
        }

        private fun toPersonGreendao(person: BasePerson): PersonGreen {
            return PersonGreen(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsGreendao(persons: List<BasePerson>): List<PersonGreen> {
            return persons.map(::toPersonGreendao)
        }

        private fun toPersonObjectbox(person: BasePerson): PersonObjectbox {
            return PersonObjectbox(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsObjectbox(persons: List<BasePerson>): List<PersonObjectbox> {
            return persons.map(::toPersonObjectbox)
        }

        private fun toPersonRoom(person: BasePerson): PersonRoom {
            return PersonRoom(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsRoom(persons: List<BasePerson>): List<PersonRoom> {
            return persons.map(::toPersonRoom)
        }

        private fun toPersonRealm(person: BasePerson): PersonRealm {
            return PersonRealm(
                person.firstName,
                person.secondName,
                person.age
            )
        }

        fun toPersonsRealm(persons: List<BasePerson>): List<PersonRealm> {
            return persons.map {
                toPersonRealm(it).also { personRealm ->
                    personRealm.id = PersonRealm.objectCounter
                }
            }
        }
    }
}