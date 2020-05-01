package com.example.database.comparison.dbms.room.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepo @Inject constructor(
    private var dao: PersonRoomDao
) : BaseRepo {

    override val name: String = "Room"

    override fun addAll(persons: List<BasePerson>) {
        persons as List<PersonRoom>

        dao.insertInTx(persons)
    }

    override fun loadAll(): List<BasePerson> {
        return dao.getAll()
    }

    override fun updateAll(persons: List<BasePerson>) {
        persons as List<PersonRoom>

        dao.updateInTx(persons)
    }

    override fun deleteAll(persons: List<BasePerson>) {
        persons as List<PersonRoom>

        dao.deleteInTx(persons)
    }

    override fun clear() {
        dao.deleteAll()
    }

    override fun transformData(data: List<BasePerson>): List<BasePerson> {
        return DataTransformer.toPersonsRoom(data)
    }
}