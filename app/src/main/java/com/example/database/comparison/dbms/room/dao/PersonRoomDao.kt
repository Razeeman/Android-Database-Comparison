package com.example.database.comparison.dbms.room.dao

import androidx.room.*
import com.example.database.comparison.dbms.room.model.PersonRoom

@Dao
interface PersonRoomDao {

    @Insert
    fun insertInTx(persons: List<PersonRoom>)

    @Query("SELECT * FROM persons")
    fun getAll(): List<PersonRoom>

    @Update
    fun updateInTx(persons: List<PersonRoom>)

    @Delete
    fun deleteInTx(persons: List<PersonRoom>)

    @Query("DELETE FROM persons")
    fun deleteAll()

}