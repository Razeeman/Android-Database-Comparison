package com.example.database.comparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.database.comparison.room.db.AppRoomDatabase
import com.example.database.comparison.room.model.Person

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CUSTOM_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val roomDao = Room.databaseBuilder(applicationContext,
            AppRoomDatabase::class.java, "room-database")
            .allowMainThreadQueries()
            .build()
            .personDao()

        // TODO System or SystemClock
        val startTime = System.currentTimeMillis()

        val person = Person(firstName = "", secondsName = "")
        roomDao.insertInTx(listOf(person))

        val testTime = System.currentTimeMillis() - startTime

        Log.d(TAG, testTime.toString())
    }
}
