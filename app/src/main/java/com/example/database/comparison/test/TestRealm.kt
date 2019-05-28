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
        const val NAME = "Realm-Unmanaged"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsRealm(data)

        var results: RealmResults<PersonRealm>? = null
        var unmanaged: List<PersonRealm> = ArrayList()
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
                results = dao.where(PersonRealm::class.java).findAll()
            }
            .run("$NAME-read", runs) {
                unmanaged = dao.copyFromRealm(results!!)
                access(unmanaged)
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                unmanaged = dao.copyFromRealm(dao.where(PersonRealm::class.java).findAll())
                updated = change(unmanaged)
            }
            .run("$NAME-update", runs) {
                dao.executeTransaction { it.insertOrUpdate(updated) }
            }

        // No delete test for unmanaged objects. Can't delete unmanaged objects by themselves.
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