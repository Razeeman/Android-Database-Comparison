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

    enum class TYPE {
        INSERT, UPDATE, DELETE
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
                stmt.bindString(2, it.secondName)
                stmt.bindLong(3, it.age.toLong())

                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

    fun insertBatched(persons: List<PersonSQLite>) {
        batched(TYPE.INSERT, persons)
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
                    .also { it.id = cursor.getLong(cursor.getColumnIndex(PersonSchema.COLUMN_ID)) }

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
                stmt.bindString(2, it.secondName)
                stmt.bindLong(3, it.age.toLong())
                stmt.bindLong(4, it.id)

                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

    fun updateBatched(persons: List<PersonSQLite>) {
        batched(TYPE.UPDATE, persons)
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
                stmt.bindLong(1, it.id)
                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

    fun deleteBatched(persons: List<PersonSQLite>) {
        batched(TYPE.DELETE, persons)
    }

    fun deleteAll() {
        database.execSQL("DELETE FROM ${PersonSchema.TABLE_NAME}")
    }

    private fun getContentValues(person: PersonSQLite): ContentValues {
        val values = ContentValues()

        values.put(PersonSchema.COLUMN_FIRST_NAME, person.firstName)
        values.put(PersonSchema.COLUMN_SECOND_NAME, person.secondName)
        values.put(PersonSchema.COLUMN_AGE, person.age)

        return values
    }

    /**
     * Creates a sqlite statement to insert/update/delete data in batches.
     *
     * @param type [TYPE] of the operation to execute.
     * @param data data to process.
     */
    private fun batched(type: TYPE, data: List<PersonSQLite>) {
        val stmtBase = when (type) {
            TYPE.INSERT -> "INSERT INTO ${PersonSchema.TABLE_NAME} " +
                    "(${PersonSchema.COLUMN_FIRST_NAME}, " +
                    "${PersonSchema.COLUMN_SECOND_NAME}, " +
                    "${PersonSchema.COLUMN_AGE}) VALUES "
            TYPE.UPDATE -> "INSERT OR REPLACE INTO ${PersonSchema.TABLE_NAME} " +
                    "(${PersonSchema.COLUMN_ID}, " +
                    "${PersonSchema.COLUMN_FIRST_NAME}, " +
                    "${PersonSchema.COLUMN_SECOND_NAME}, " +
                    "${PersonSchema.COLUMN_AGE}) VALUES "
            TYPE.DELETE -> "DELETE FROM ${PersonSchema.TABLE_NAME} WHERE " +
                    "${PersonSchema.COLUMN_ID} IN ("
        }

        val columns = when (type) {
            TYPE.INSERT -> 3 // Number of columns in the table schema minus id column.
            TYPE.UPDATE -> 4 // Number of columns in the table schema.
            TYPE.DELETE -> 1 // Only id column is used.
        }

        val stmtPart = when (type) {
            TYPE.INSERT -> "(?, ?, ?)"    // Number of columns in the table schema minus id column.
            TYPE.UPDATE -> "(?, ?, ?, ?)" // Number of columns in the table schema.
            TYPE.DELETE -> "?"            // Only id column is used.
        }

        val total = data.size         // Total number of data points to insert/update/delete.
        var processed = 0             // Processed number of data points.
        var rest: Int                 // Number of data points yet to process.
        var batchSize = 999/columns   // Maximum batch of data points to process in one statement.
        val builder = StringBuilder()
        var id: Long

        // Suppressed because SparseArray is slower.
        @SuppressLint("UseSparseArrays")
        val stmtCache = HashMap<Int, SQLiteStatement>()
        var stmt: SQLiteStatement

        database.transaction {

            while (processed < total) {

                rest = total - processed
                batchSize = if (rest > batchSize) batchSize else rest

                // Prepare part of the statement that hold value bindings, create new or get from cache.
                stmt = stmtCache.getOrElse(batchSize) {
                    builder.clear()
                    for (i: Int in 0 until batchSize) {
                        if (i > 0) builder.append(", ")
                        builder.append(stmtPart)
                    }
                    if (type == TYPE.DELETE) builder.append(")")
                    database.compileStatement(stmtBase + builder.toString())
                        .also { stmtCache[batchSize] = it }
                }

                // Bind data points to bindings in the statement.
                for (i: Int in 0 until batchSize) {
                    when (type) {
                        TYPE.INSERT -> {
                            stmt.bindString(columns*i + 1, data[processed].firstName)
                            stmt.bindString(columns*i + 2, data[processed].secondName)
                            stmt.bindLong(columns*i + 3, data[processed].age.toLong())
                        }
                        TYPE.UPDATE -> {
                            id = data[processed].id
                            // Id of zero means that it is a new object, so it needs to be inserted.
                            // Argument that are not bound will be defaulted to null and for id it means insert.
                            if (id > 0) stmt.bindLong(columns*i + 1, id)
                            stmt.bindString(columns*i + 2, data[processed].firstName)
                            stmt.bindString(columns*i + 3, data[processed].secondName)
                            stmt.bindLong(columns*i + 4, data[processed].age.toLong())
                        }
                        TYPE.DELETE -> {
                            stmt.bindLong(i + 1, data[processed].id)
                        }
                    }
                    processed++
                }

                stmt.execute()
                stmt.clearBindings()
            }
        }
    }

}