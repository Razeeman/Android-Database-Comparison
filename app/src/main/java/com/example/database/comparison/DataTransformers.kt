package com.example.database.comparison

import com.example.database.comparison.model.Person

class DataTransformers private constructor() {

    companion object {

        fun toRoomPerson(person: Person): com.example.database.comparison.room.model.Person {
            return com.example.database.comparison.room.model.Person(
                person.firstName,
                person.secondsName,
                person.age)
        }

        fun toRoomPersons(persons: List<Person>): List<com.example.database.comparison.room.model.Person> {
            return persons.map { toRoomPerson(it) }
        }

        fun toGreendaoPerson(person: Person): com.example.database.comparison.greendao.model.Person {
            return com.example.database.comparison.greendao.model.Person(
                person.firstName,
                person.secondsName,
                person.age)
        }

        fun toGreendaoPersons(persons: List<Person>): List<com.example.database.comparison.greendao.model.Person> {
            return persons.map { toGreendaoPerson(it) }
        }

    }

}