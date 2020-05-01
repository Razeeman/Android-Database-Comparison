package com.example.database.comparison.test.old

import com.example.database.comparison.base.BaseTest
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import java.util.*

class TestSQLiteRaw(
    private val runner: Runner,
    private val dao: PersonSQLiteDao
) : BaseTest {

    companion object {
        const val NAME = "SQLiteRaw"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsSQLite(data)
        var reloaded: List<PersonSQLite> = ArrayList()
        var updated: List<PersonSQLite> = ArrayList()

        runner
            .beforeEach { dao.deleteAll() }
            .run("$NAME-create", runs) { dao.insertRaw(persons) }

        // Read test the same as SQLite.
        runner
            .before {
                dao.deleteAll()
                dao.insertRaw(persons)
            }
            .run("$NAME-read", runs) {
                reloaded = dao.getAll()
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertRaw(persons)
                reloaded = dao.getAll()
                updated = change(reloaded)
            }
            .run("$NAME-update", runs) { dao.updateRaw(updated) }

        runner
            .beforeEach {
                dao.deleteAll()
                dao.insertRaw(persons)
                reloaded = dao.getAll()
            }
            .run("$NAME-delete", runs) { dao.deleteRaw(reloaded) }
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