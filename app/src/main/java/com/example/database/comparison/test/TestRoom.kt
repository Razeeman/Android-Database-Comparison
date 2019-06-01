package com.example.database.comparison.test

import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import java.util.*

class TestRoom(private val runner: Runner, private val dao : PersonRoomDao)
    : BaseTest {

    companion object {
        const val NAME = "Room"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsRoom(data)
        var reloaded: List<PersonRoom> = ArrayList()
        var updated: List<PersonRoom> = ArrayList()

        runner
            .beforeEach { dao.deleteAll() }
            .run("$NAME-create", runs) { dao.insertInTx(persons) }

        runner
            .before {
                dao.deleteAll()
                dao.insertInTx(persons)
            }
            .run("$NAME-read", runs) {
                reloaded = dao.getAll()
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertInTx(persons)
                reloaded = dao.getAll()
                updated = change(reloaded)
            }
            .run("$NAME-update", runs) { dao.updateInTx(updated) }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertInTx(persons)
                reloaded = dao.getAll()
            }
            .run("$NAME-delete", runs) { dao.deleteInTx(reloaded) }
    }

    private fun access(persons: List<PersonRoom>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    private fun change(persons: List<PersonRoom>): List<PersonRoom> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}