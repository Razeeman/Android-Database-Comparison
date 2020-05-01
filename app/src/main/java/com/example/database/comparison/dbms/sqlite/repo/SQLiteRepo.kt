package com.example.database.comparison.dbms.sqlite.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SQLiteRepo @Inject constructor(
    private var dao: PersonSQLiteDao
) : BaseRepo {

    override val name: String = "SQLite"

    override fun addAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.insertInTx(persons)
    }

    override fun loadAll(): List<BasePerson> {
        return dao.getAll()
    }

    override fun updateAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.updateInTx(persons)
    }

    override fun deleteAll(persons: List<BasePerson>) {
        persons as List<PersonSQLite>

        dao.deleteInTx(persons)
    }

    override fun clear() {
        dao.deleteAll()
    }

    override fun transformData(data: List<BasePerson>): List<BasePerson> {
        return DataTransformer.toPersonsSQLite(data)
    }
}