package com.example.database.comparison.sqlite.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction
import androidx.room.Delete
import com.example.database.comparison.sqlite.db.PersonSchema
import com.example.database.comparison.sqlite.model.PersonSQLite


class PersonSQLiteDao private constructor(private var database: SQLiteDatabase) {

    companion object {

        // Dao instance.
        @Volatile private var instance: PersonSQLiteDao? = null

        // Singleton instantiation.
        fun get(database: SQLiteDatabase): PersonSQLiteDao {
            return instance ?: synchronized(this) {
                instance ?: PersonSQLiteDao(database).also { instance = it }
            }
        }
    }

    fun insertInTx(persons: List<PersonSQLite>) {
        database.transaction {
            for (person in persons) {
                insert(PersonSchema.TABLE_NAME,
                    null,
                    getContentValues(person))
            }
        }
    }

    fun getAll(): List<PersonSQLite> {
        val persons = ArrayList<PersonSQLite>()

        val cursor = database.query(
            PersonSchema.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null)

        cursor.use {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(PersonSchema.COLUMN_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(PersonSchema.COLUMN_FIRST_NAME))
                val secondName = cursor.getString(cursor.getColumnIndex(PersonSchema.COLUMN_SECOND_NAME))
                val age = cursor.getInt(cursor.getColumnIndex(PersonSchema.COLUMN_AGE))

                persons.add(PersonSQLite(firstName, secondName, age).also { it.id = id })

                cursor.moveToNext()
            }
        }

        return persons
    }

    fun updateInTx(persons: List<PersonSQLite>) {
        database.transaction {
            for (person in persons) {
                update(PersonSchema.TABLE_NAME,
                    getContentValues(person),
                    PersonSchema.COLUMN_ID + " = ?",
                    arrayOf(person.id.toString()))
            }
        }
    }

    @Delete
    fun deleteInTx(persons: List<PersonSQLite>) {
        database.transaction {
            for (person in persons) {
                delete(PersonSchema.TABLE_NAME,
                    PersonSchema.COLUMN_ID + " = ?",
                    arrayOf(person.id.toString()))
            }
        }
    }

    fun deleteAll() {
        database.execSQL("DELETE FROM ${PersonSchema.TABLE_NAME}")
    }

    private fun getContentValues(person: PersonSQLite): ContentValues {
        val values = ContentValues()

        values.put(PersonSchema.COLUMN_ID, person.id)
        values.put(PersonSchema.COLUMN_FIRST_NAME, person.firstName)
        values.put(PersonSchema.COLUMN_SECOND_NAME, person.secondsName)
        values.put(PersonSchema.COLUMN_AGE, person.age)

        return values
    }

}