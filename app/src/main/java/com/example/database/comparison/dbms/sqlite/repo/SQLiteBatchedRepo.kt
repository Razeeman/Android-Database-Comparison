package com.example.database.comparison.dbms.sqlite.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SQLiteBatchedRepo @Inject constructor(
    private var dao: PersonSQLiteDao
) : BaseRepo<PersonSQLite> {

    override val name: String = "SQLiteBatched"

    override fun addAll(persons: List<PersonSQLite>) {
        dao.insertBatched(persons)
    }

    override fun loadAll(): List<PersonSQLite> {
        return dao.getAllRaw()
    }

    override fun updateAll(persons: List<PersonSQLite>) {
        dao.updateBatched(persons)
    }

    override fun deleteAll(persons: List<PersonSQLite>) {
        dao.deleteBatched(persons)
    }

    override fun clear() {
        dao.deleteAllRaw()
    }

    override fun transformData(data: List<Person>): List<PersonSQLite> {
        return DataTransformer.toPersonsSQLite(data)
    }
}