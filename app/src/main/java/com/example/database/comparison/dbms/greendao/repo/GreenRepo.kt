package com.example.database.comparison.dbms.greendao.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.greendao.PersonGreenDao
import com.example.database.comparison.dbms.greendao.model.PersonGreen
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GreenRepo @Inject constructor(
    private var dao: PersonGreenDao
) : BaseRepo<PersonGreen> {

    override val name: String = "GreenDao"

    override fun addAll(persons: List<PersonGreen>) {
        dao.insertInTx(persons)
    }

    override fun loadAll(): List<PersonGreen> {
        return dao.loadAll()
    }

    override fun updateAll(persons: List<PersonGreen>) {
        dao.updateInTx(persons)
    }

    override fun deleteAll(persons: List<PersonGreen>) {
        dao.deleteInTx(persons)
    }

    override fun clear() {
        dao.deleteAll()
    }

    override fun transformData(data: List<Person>): List<PersonGreen> {
        return DataTransformer.toPersonsGreendao(data)
    }
}