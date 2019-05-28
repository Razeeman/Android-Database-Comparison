package com.example.database.comparison.test

import com.example.database.comparison.dbms.realm.PersonRealm
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import io.realm.Realm
import java.util.*

class TestRealmManaged(private val runner: Runner, private val dao : Realm)
    : BaseTest {

    companion object {
        const val NAME = "Realm-Managed"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsRealm(data)

        var managed: List<PersonRealm> = ArrayList()

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
            }
            .run("$NAME-create", runs) {
                dao.executeTransaction { it.copyToRealm(persons) }
            }

        runner
            .before {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
            }
            .run("$NAME-read", runs) {
                managed = dao.where(PersonRealm::class.java).findAll()
                access(managed)
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                managed = dao.copyToRealm(persons)
            }
            .run("$NAME-update", runs) {
                dao.executeTransaction { change(managed) }
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                managed = dao.copyToRealm(persons)
            }
            .run("$NAME-delete", runs) {
                dao.executeTransaction { managed.forEach { it.deleteFromRealm() } }
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