package com.example.database.comparison.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.comparison.room.dao.PersonDao
import com.example.database.comparison.room.model.Person

@Database(entities = [Person::class], version = 1)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun personDao(): PersonDao

}