package com.example.database.comparison.test

import com.example.database.comparison.dbms.objectbox.PersonObjectbox
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import io.objectbox.Box
import java.util.*

class TestObjectbox(private val runner: Runner, private val dao : Box<PersonObjectbox>)
    : BaseTest {

    companion object {
        const val NAME = "ObjectBox"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsObjectbox(data)
        var reloaded: List<PersonObjectbox> = ArrayList()
        var updated: List<PersonObjectbox> = ArrayList()

        runner
            .beforeEach { dao.removeAll() }
            .run("$NAME-create", runs) { dao.put(persons) }

        runner
            .before {
                dao.removeAll()
                dao.put(persons)
            }
            .run("$NAME-read", runs) {
                reloaded = dao.all
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.removeAll()
                dao.put(persons)
                reloaded = dao.all
                updated = change(reloaded)
            }
            .run("$NAME-update", runs) { dao.put(updated) }

        runner
            .beforeEach {
                dao.removeAll()
                dao.put(persons)
                reloaded = dao.all
            }
            .run("$NAME-delete", runs) { dao.remove(reloaded) }
    }

    private fun access(persons: List<PersonObjectbox>) {
        persons.forEach {
            it.firstName
            it.secondsName
            it.age
        }
    }

    private fun change(persons: List<PersonObjectbox>): List<PersonObjectbox> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}