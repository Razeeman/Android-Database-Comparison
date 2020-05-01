package com.example.database.comparison.dbms.room.repo

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.model.PersonRoom
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.DataTransformer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepo @Inject constructor(
    private var dao: PersonRoomDao
) : BaseRepo<PersonRoom> {

    override val name: String = "Room"

    override fun addAll(persons: List<PersonRoom>) {
        dao.insertInTx(persons)
    }

    override fun loadAll(): List<PersonRoom> {
        return dao.getAll()
    }

    override fun updateAll(persons: List<PersonRoom>) {
        dao.updateInTx(persons)
    }

    override fun deleteAll(persons: List<PersonRoom>) {
        dao.deleteInTx(persons)
    }

    override fun clear() {
        dao.deleteAll()
    }

    override fun transformData(data: List<Person>): List<PersonRoom> {
        return DataTransformer.toPersonsRoom(data)
    }
}