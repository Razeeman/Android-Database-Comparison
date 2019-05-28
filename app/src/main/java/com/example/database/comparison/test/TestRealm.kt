package com.example.database.comparison.test

import com.example.database.comparison.dbms.realm.PersonRealm
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class TestRealm(private val runner: Runner, private val dao : Realm)
    : BaseTest {

    companion object {
        const val NAME = "Realm"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsRealm(data)
        var results: RealmResults<PersonRealm>? = null
        var reloaded: List<PersonRealm> = ArrayList()
        var updated: List<PersonRealm> = ArrayList()

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
            }
            .run("$NAME-create", runs) {
                dao.executeTransaction { it.insert(persons) }
            }

        runner
            .before {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                dao.executeTransaction { results = it.where(PersonRealm::class.java).findAll() }
            }
            .run("$NAME-read", runs) {
                dao.executeTransaction { reloaded = it.copyFromRealm(results!!) }
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                dao.executeTransaction { reloaded = it.copyFromRealm(it.where(PersonRealm::class.java).findAll()) }
                updated = change(reloaded)
            }
            .run("$NAME-update", runs) { dao.insertOrUpdate(updated) }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                dao.executeTransaction { reloaded = it.where(PersonRealm::class.java).findAll() }
            }
            .run("$NAME-delete", runs) {
                dao.executeTransaction { reloaded.forEach { it.deleteFromRealm() } }
            }
    }

    private fun access(persons: List<PersonRealm>) {
        persons.forEach {
            it.firstName
            it.secondsName
            it.age
        }
    }

    private fun change(persons: List<PersonRealm>): List<PersonRealm> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}