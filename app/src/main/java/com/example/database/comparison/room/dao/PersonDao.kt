package com.example.database.comparison.room.dao

import androidx.room.*
import com.example.database.comparison.room.model.Person

@Dao
interface PersonDao {

    @Insert
    fun insertInTx(persons: List<Person>)

    @Query("SELECT * FROM persons")
    fun getAll(): List<Person>

    @Update
    fun updateInTx(persons: List<Person>)

    @Delete
    fun deleteInTx(persons: List<Person>)

}