package com.example.database.comparison.dbms.greendao.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.greendao.PersonGreenDao
import com.example.database.comparison.dbms.greendao.model.PersonGreen
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GreenRepo @Inject constructor(
    private var dao: PersonGreenDao
) : BaseRepo {

    override val name: String = "GreenDao"

    override fun addAll(persons: List<BasePerson>) {
        persons as List<PersonGreen>

        dao.insertInTx(persons)
    }

    override fun loadAll(): List<BasePerson> {
        return dao.loadAll()
    }

    override fun updateAll(persons: List<BasePerson>) {
        persons as List<PersonGreen>

        dao.updateInTx(persons)
    }

    override fun deleteAll(persons: List<BasePerson>) {
        persons as List<PersonGreen>

        dao.deleteInTx(persons)
    }

    override fun clear() {
        dao.deleteAll()
    }

    override fun transformData(data: List<BasePerson>): List<BasePerson> {
        return DataTransformer.toPersonsGreendao(data)
    }
}