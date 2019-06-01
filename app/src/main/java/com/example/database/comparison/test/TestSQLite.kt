package com.example.database.comparison.test

import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import java.util.ArrayList

class TestSQLite(private val runner: Runner, private val dao : PersonSQLiteDao)
    : BaseTest {

    companion object {
        const val NAME = "SQLite"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsSQLite(data)
        var reloaded: List<PersonSQLite> = ArrayList()
        var updated: List<PersonSQLite> = ArrayList()

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

    private fun access(persons: List<PersonSQLite>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    private fun change(persons: List<PersonSQLite>): List<PersonSQLite> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}