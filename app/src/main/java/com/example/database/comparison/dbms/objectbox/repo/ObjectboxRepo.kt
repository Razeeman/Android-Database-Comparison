package com.example.database.comparison.dbms.objectbox.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataTransformer
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObjectboxRepo @Inject constructor(
    private var dao: Box<PersonObjectbox>
) : BaseRepo {

    override val name: String = "ObjectBox"

    override fun addAll(persons: List<BasePerson>) {
        persons as List<PersonObjectbox>

        dao.put(persons)
    }

    override fun loadAll(): List<BasePerson> {
        return dao.all
    }

    override fun updateAll(persons: List<BasePerson>) {
        persons as List<PersonObjectbox>

        dao.put(persons)
    }

    override fun deleteAll(persons: List<BasePerson>) {
        persons as List<PersonObjectbox>

        dao.remove(persons)
    }

    override fun clear() {
        dao.removeAll()
    }

    override fun transformData(data: List<BasePerson>): List<BasePerson> {
        return DataTransformer.toPersonsObjectbox(data)
    }
}