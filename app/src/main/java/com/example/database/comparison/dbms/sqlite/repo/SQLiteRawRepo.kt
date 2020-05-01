package com.example.database.comparison.dbms.sqlite.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SQLiteRawRepo @Inject constructor(
    private var dao: PersonSQLiteDao
) : BaseRepo {

    override val name: String = "SQLiteRaw"

    override fun addAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.insertRaw(persons)
    }

    override fun loadAll(): List<BasePerson> {
        return dao.getAllRaw()
    }

    override fun updateAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.updateRaw(persons)
    }

    override fun deleteAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.deleteRaw(persons)
    }

    override fun clear() {
        dao.deleteAllRaw()
    }

    override fun transformData(data: List<BasePerson>): List<BasePerson> {
        return DataTransformer.toPersonsSQLite(data)
    }
}