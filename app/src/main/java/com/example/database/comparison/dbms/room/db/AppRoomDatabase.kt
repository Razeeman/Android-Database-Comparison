package com.example.database.comparison.dbms.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.model.PersonRoom

@Database(entities = [PersonRoom::class], version = 1)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun personRoomDao(): PersonRoomDao

}