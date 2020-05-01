package com.example.database.comparison.dbms.realm.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.realm.model.PersonRealm
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataProvider
import com.example.database.comparison.util.DataTransformer
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmRepo @Inject constructor(
    private var dao: Realm
) : BaseRepo<PersonRealm> {

    override val name: String = "Realm"

    override fun addAll(persons: List<PersonRealm>) {
        dao.executeTransaction {
            it.insert(persons)
        }
    }

    override fun loadAll(): List<PersonRealm> {
        return dao.where(PersonRealm::class.java).findAll()
    }

    override fun updateAll(persons: List<PersonRealm>) {
        // Realm updates managed objects in place
        change(persons)
    }

    override fun deleteAll(persons: List<PersonRealm>) {
        // Need managed objects
        persons as RealmResults<PersonRealm>

        dao.executeTransaction {
            persons.deleteAllFromRealm()
        }
    }

    override fun clear() {
        dao.executeTransaction {
            it.delete(PersonRealm::class.java)
        }
    }

    override fun transformData(data: List<Person>): List<PersonRealm> {
        return DataTransformer.toPersonsRealm(data)
    }

    override fun change(persons: List<PersonRealm>): List<PersonRealm> {
        dao.executeTransaction {
            persons.map { it.also { it.age = DataProvider.getInt() } }
        }
        return persons
    }
}