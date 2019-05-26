package com.example.database.comparison.sqlite.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppSQLiteDatabase private constructor(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "sqlite-database"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_PERSONS = "create table " +
                PersonSchema.TABLE_NAME + "(_id long primary key autoincrement, " +
                PersonSchema.COLUMN_ID + ", " +
                PersonSchema.COLUMN_FIRST_NAME + ", " +
                PersonSchema.COLUMN_SECOND_NAME + ", " +
                PersonSchema.COLUMN_AGE + ")"

        // Database instance.
        @Volatile private var instance: SQLiteDatabase? = null

        // Singleton instantiation.
        fun get(context: Context): SQLiteDatabase {
            return instance ?: synchronized(this) {
                instance ?: AppSQLiteDatabase(context).writableDatabase.also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_PERSONS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${PersonSchema.TABLE_NAME}")
    }
}