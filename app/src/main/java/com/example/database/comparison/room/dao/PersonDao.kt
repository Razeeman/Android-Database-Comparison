package com.example.database.comparison.room.dao

import androidx.room.*
import com.example.database.comparison.room.model.Person

@Dao
interface PersonDao {

    @Insert
    fun insert(person: Person)

    @Query("SELECT * FROM persons WHERE id LIKE :personId")
    fun getById(personId: Long): Person

    @Update
    fun update(person: Person)

    @Delete
    fun delete(person: Person)

}