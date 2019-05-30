package com.example.database.comparison.test

import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import java.util.ArrayList

class TestSQLiteBatched(private val runner: Runner, private val dao : PersonSQLiteDao)
    : BaseTest {

    companion object {
        const val NAME = "SQLiteBatched"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsSQLite(data)
        var reloaded: List<PersonSQLite> = ArrayList()
        var updated: List<PersonSQLite> = ArrayList()

        runner
            .beforeEach { dao.deleteAll() }
            .run("$NAME-create", runs) { dao.insertBatched(persons) }

        // Read test the same as SQLite.

        // TODO update batched.

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertInTx(persons)
                reloaded = dao.getAll()
            }
            .run("$NAME-delete", runs) { dao.deleteBatched(reloaded) }
    }

    private fun change(persons: List<PersonSQLite>): List<PersonSQLite> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}