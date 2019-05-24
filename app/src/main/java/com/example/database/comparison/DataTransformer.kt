package com.example.database.comparison

import com.example.database.comparison.greendao.model.PersonGreen
import com.example.database.comparison.model.Person
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

    }

}