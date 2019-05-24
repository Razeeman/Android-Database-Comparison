package com.example.database.comparison

import com.example.database.comparison.greendao.model.PersonGreen
import com.example.database.comparison.model.Person
import com.example.database.comparison.realm.model.PersonRealm
import com.example.database.comparison.room.model.PersonRoom

class DataTransformer private constructor() {

    companion object {

        fun toPersonRoom(person: Person): PersonRoom {
            return PersonRoom(
                person.firstName,
                person.secondsName,
                person.age)
        }

        fun toPersonsRoom(persons: List<Person>): List<PersonRoom> {
            return persons.map { toPersonRoom(it) }
        }

        fun toPersonGreendao(person: Person): PersonGreen {
            return PersonGreen(
                person.firstName,
                person.secondsName,
                person.age
            )
        }

        fun toPersonsGreendao(persons: List<Person>): List<PersonGreen> {
            return persons.map { toPersonGreendao(it) }
        }

        fun toPersonRealm(person: Person): PersonRealm {
            return PersonRealm(
                person.firstName,
                person.secondsName,
                person.age
            )
        }

        fun toPersonsRealm(persons: List<Person>): List<PersonRealm> {
            return persons.map { toPersonRealm(it).also {
                    personRealm -> personRealm.id = PersonRealm.objectCounter }
            }
        }

    }

}