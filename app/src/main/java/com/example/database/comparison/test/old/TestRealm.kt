package com.example.database.comparison.test.old

import com.example.database.comparison.base.BaseTest
import com.example.database.comparison.dbms.realm.model.PersonRealm
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import com.example.database.comparison.util.Runner
import io.realm.Realm
import io.realm.RealmResults

class TestRealm(
    private val runner: Runner,
    private val dao: Realm
) : BaseTest {

    companion object {
        const val NAME = "Realm"
    }

    override fun run(runs: Int, data: List<Person>) {

        val persons = DataTransformer.toPersonsRealm(data)
        var reloaded: List<PersonRealm> = emptyList()

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
            }
            .run("$NAME-read", runs) {
                reloaded = dao.where(PersonRealm::class.java).findAll()
                access(reloaded)
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                reloaded = dao.where(PersonRealm::class.java).findAll()
            }
            .run("$NAME-update", runs) {
                dao.executeTransaction { change(reloaded) }
            }

        runner
            .beforeEach {
                dao.executeTransaction { it.delete(PersonRealm::class.java) }
                dao.executeTransaction { it.insert(persons) }
                reloaded = dao.where(PersonRealm::class.java).findAll()
            }
            .run("$NAME-delete", runs) {
                dao.executeTransaction { (reloaded as RealmResults).deleteAllFromRealm() }
            }
    }

    private fun access(persons: List<PersonRealm>) {
        persons.forEach {
            it.firstName
            it.secondName
            it.age
        }
    }

    private fun change(persons: List<PersonRealm>): List<PersonRealm> {
        return persons.map { it.also { person -> person.age = 0 } }
    }

}