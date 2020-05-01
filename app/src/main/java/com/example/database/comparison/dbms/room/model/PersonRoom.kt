package com.example.database.comparison.dbms.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.comparison.model.BasePerson

@Entity(tableName = "persons")
data class PersonRoom(
    @ColumnInfo(name = "first_name") override var firstName: String,
    @ColumnInfo(name = "second_name") override var secondName: String,
    @ColumnInfo(name = "age") override var age: Int
) : BasePerson {

    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0
}