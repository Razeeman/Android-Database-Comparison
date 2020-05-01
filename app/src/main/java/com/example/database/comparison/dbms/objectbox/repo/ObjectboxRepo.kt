package com.example.database.comparison.dbms.objectbox.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObjectboxRepo @Inject constructor(
    private var dao: Box<PersonObjectbox>
) : BaseRepo<PersonObjectbox> {

    override val name: String = "ObjectBox"

    override fun addAll(persons: List<PersonObjectbox>) {
        dao.put(persons)
    }

    override fun loadAll(): List<PersonObjectbox> {
        return dao.all
    }

    override fun updateAll(persons: List<PersonObjectbox>) {
        dao.put(persons)
    }

    override fun deleteAll(persons: List<PersonObjectbox>) {
        dao.remove(persons)
    }

    override fun clear() {
        dao.removeAll()
    }

    override fun transformData(data: List<Person>): List<PersonObjectbox> {
        return DataTransformer.toPersonsObjectbox(data)
    }
}