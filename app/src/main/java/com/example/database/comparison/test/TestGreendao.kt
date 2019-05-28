package com.example.database.comparison.test

import com.example.database.comparison.dbms.greendao.PersonGreen
import com.example.database.comparison.dbms.greendao.PersonGreenDao
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import java.util.*

class TestGreendao(private val runner: Runner, private val dao : PersonGreenDao)
    : BaseTest {

    companion object {
        const val NAME = "GreenDao"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsGreendao(data)
        var reloaded: List<PersonGreen> = ArrayList()
        var updated: List<PersonGreen> = ArrayList()

        runner
            .beforeEach { dao.deleteAll() }
            .run("$NAME-create", runs) { dao.insertInTx(persons) }

        runner
            .before {
                dao.deleteAll()
                dao.insertInTx(persons)
            }
            .run("$NAME-read", runs) {
                reloaded = dao.loadAll()
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertInTx(persons)
                reloaded = dao.loadAll()
                updated = change(reloaded)
            }
            .run("$NAME-update", runs) { dao.updateInTx(updated) }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertInTx(persons)
                reloaded = dao.loadAll()
            }
            .run("$NAME-delete", runs) { dao.deleteInTx(reloaded) }
    }

    private fun access(persons: List<PersonGreen>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    private fun change(persons: List<PersonGreen>): List<PersonGreen> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}