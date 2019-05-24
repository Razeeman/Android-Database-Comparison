package com.example.database.comparison.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonRoom (
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "seconds_name") var secondsName: String,
    @ColumnInfo(name = "age") var age: Int
) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

}