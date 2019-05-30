package com.example.database.comparison.dbms.sqlite.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
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

    // TODO generalize batched insert, update and delete.
    fun insertBatched(persons: List<PersonSQLite>) {
        val insert = "INSERT INTO ${PersonSchema.TABLE_NAME} " +
                "(${PersonSchema.COLUMN_FIRST_NAME}, " +
                "${PersonSchema.COLUMN_SECOND_NAME}, " +
                "${PersonSchema.COLUMN_AGE}) VALUES "

        val columns = 3
        val insertions = persons.size
        var inserted = 0
        var batchSize = 999/columns

        // Suppressed because SparseArray is slower.
        @SuppressLint("UseSparseArrays")
        val stmtCache = HashMap<Int, SQLiteStatement>()
        var stmt: SQLiteStatement

        database.transaction {

            while (inserted < insertions) {

                val rest = insertions - inserted
                batchSize = if (rest > batchSize) batchSize else rest

                stmt = stmtCache.getOrElse(batchSize) {
                    val builder = StringBuilder()
                    for (i: Int in 0 until batchSize) {
                        if (i > 0) builder.append(", ")
                        builder.append("(?, ?, ?)")
                    }
                    database.compileStatement(insert + builder.toString())
                        .also { stmtCache[batchSize] = it }
                }

                for (i: Int in 0 until batchSize) {
                    stmt.bindString(columns*i + 1, persons[inserted].firstName)
                    stmt.bindString(columns*i + 2, persons[inserted].secondsName)
                    stmt.bindLong(columns*i + 3, persons[inserted].age.toLong())
                    inserted++
                }

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

    fun updateBatched(persons: List<PersonSQLite>) {
        val insert = "INSERT OR REPLACE INTO ${PersonSchema.TABLE_NAME} " +
                "(${PersonSchema.COLUMN_ID}, " +
                "${PersonSchema.COLUMN_FIRST_NAME}, " +
                "${PersonSchema.COLUMN_SECOND_NAME}, " +
                "${PersonSchema.COLUMN_AGE}) VALUES "

        val columns = 4
        val insertions = persons.size
        var inserted = 0
        var batchSize = 999/columns

        // Suppressed because SparseArray is slower.
        @SuppressLint("UseSparseArrays")
        val stmtCache = HashMap<Int, SQLiteStatement>()
        var stmt: SQLiteStatement

        database.transaction {

            while (inserted < insertions) {

                val rest = insertions - inserted
                batchSize = if (rest > batchSize) batchSize else rest

                stmt = stmtCache.getOrElse(batchSize) {
                    val builder = StringBuilder()
                    for (i: Int in 0 until batchSize) {
                        if (i > 0) builder.append(", ")
                        builder.append("(?, ?, ?, ?)")
                    }
                    database.compileStatement(insert + builder.toString())
                        .also { stmtCache[batchSize] = it }
                }

                for (i: Int in 0 until batchSize) {
                    val id = persons[inserted].id
                    if (id > 0) stmt.bindLong(columns*i + 1, id.toLong())
                    stmt.bindString(columns*i + 2, persons[inserted].firstName)
                    stmt.bindString(columns*i + 3, persons[inserted].secondsName)
                    stmt.bindLong(columns*i + 4, persons[inserted].age.toLong())
                    inserted++
                }

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

    fun deleteBatched(persons: List<PersonSQLite>) {
        val delete = "DELETE FROM ${PersonSchema.TABLE_NAME} WHERE ${PersonSchema.COLUMN_ID} IN ("

        val insertions = persons.size
        var inserted = 0
        var batchSize = 999

        // Suppressed because SparseArray is slower.
        @SuppressLint("UseSparseArrays")
        val stmtCache = HashMap<Int, SQLiteStatement>()
        var stmt: SQLiteStatement

        database.transaction {

            while (inserted < insertions) {

                val rest = insertions - inserted
                batchSize = if (rest > batchSize) batchSize else rest

                stmt = stmtCache.getOrElse(batchSize) {
                    val builder = StringBuilder()
                    for (i: Int in 0 until batchSize) {
                        if (i > 0) builder.append(", ")
                        builder.append("?")
                    }
                    builder.append(")")
                    database.compileStatement(delete + builder.toString())
                        .also { stmtCache[batchSize] = it }
                }

                for (i: Int in 0 until batchSize) {
                    stmt.bindLong(i + 1, persons[inserted].id.toLong())
                    inserted++
                }

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