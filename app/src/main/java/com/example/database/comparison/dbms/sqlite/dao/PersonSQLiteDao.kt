package com.example.database.comparison.dbms.sqlite.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction
import androidx.room.Delete
import com.example.database.comparison.dbms.sqlite.db.PersonSchema
import com.example.database.comparison.dbms.sqlite.model.PersonSQLite

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

    fun insertRaw(persons: List<PersonSQLite>) {
        val insert = "INSERT INTO ${PersonSchema.TABLE_NAME} " +
                "(${PersonSchema.COLUMN_FIRST_NAME}, " +
                "${PersonSchema.COLUMN_SECOND_NAME}, " +
                "${PersonSchema.COLUMN_AGE}) VALUES (?, ?, ?)"
        val stmt = database.compileStatement(insert)

        database.transaction {
            persons.forEach {
                stmt.bindString(1, it.firstName)
                stmt.bindString(2, it.secondsName)
                stmt.bindLong(3, it.age.toLong())

                stmt.execute()
                stmt.clearBindings()
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
            cursor.moveToPosition(-1)
            while (cursor.moveToNext()) {
                val person = PersonSQLite(
                    cursor.getString(cursor.getColumnIndex(PersonSchema.COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(PersonSchema.COLUMN_SECOND_NAME)),
                    cursor.getInt(cursor.getColumnIndex(PersonSchema.COLUMN_AGE)))
                    .also { it.id = cursor.getInt(cursor.getColumnIndex(PersonSchema.COLUMN_ID)) }

                persons.add(person)
            }
        }

        return persons
    }

    fun updateInTx(persons: List<PersonSQLite>) {
        database.transaction {
            for (person in persons) {
                update(PersonSchema.TABLE_NAME,
                    getContentValues(person),
                    "${PersonSchema.COLUMN_ID} = ${person.id}",
                    null)
            }
        }
    }

    fun updateRaw(persons: List<PersonSQLite>) {
        val update = "UPDATE ${PersonSchema.TABLE_NAME} SET " +
                "${PersonSchema.COLUMN_FIRST_NAME}=?, " +
                "${PersonSchema.COLUMN_SECOND_NAME}=?, " +
                "${PersonSchema.COLUMN_AGE}=? WHERE ${PersonSchema.COLUMN_ID}=?"
        val stmt = database.compileStatement(update)

        database.transaction {
            persons.forEach {
                stmt.bindString(1, it.firstName)
                stmt.bindString(2, it.secondsName)
                stmt.bindLong(3, it.age.toLong())
                stmt.bindLong(4, it.id.toLong())

                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

    @Delete
    fun deleteInTx(persons: List<PersonSQLite>) {
        database.transaction {
            for (person in persons) {
                delete(PersonSchema.TABLE_NAME,
                    "${PersonSchema.COLUMN_ID} = ${person.id}",
                    null)
            }
        }
    }

    fun deleteRaw(persons: List<PersonSQLite>) {
        val delete = "DELETE FROM ${PersonSchema.TABLE_NAME} WHERE ${PersonSchema.COLUMN_ID}=?"
        val stmt = database.compileStatement(delete)

        database.transaction {
            persons.forEach {
                stmt.bindLong(1, it.id.toLong())
                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

    fun deleteAll() {
        database.execSQL("DELETE FROM ${PersonSchema.TABLE_NAME}")
    }

    private fun getContentValues(person: PersonSQLite): ContentValues {
        val values = ContentValues()

        values.put(PersonSchema.COLUMN_FIRST_NAME, person.firstName)
        values.put(PersonSchema.COLUMN_SECOND_NAME, person.secondsName)
        values.put(PersonSchema.COLUMN_AGE, person.age)

        return values
    }

}