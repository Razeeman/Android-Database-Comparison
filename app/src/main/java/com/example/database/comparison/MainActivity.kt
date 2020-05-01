package com.example.database.comparison

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.database.comparison.dbms.greendao.repo.GreenRepo
import com.example.database.comparison.dbms.objectbox.repo.ObjectboxRepo
import com.example.database.comparison.dbms.realm.repo.RealmRepo
import com.example.database.comparison.dbms.room.repo.RoomRepo
import com.example.database.comparison.dbms.sqlite.repo.SQLiteBatchedRepo
import com.example.database.comparison.dbms.sqlite.repo.SQLiteRawRepo
import com.example.database.comparison.dbms.sqlite.repo.SQLiteRepo
import com.example.database.comparison.test.Test
import com.example.database.comparison.util.DataProvider
import com.example.database.comparison.util.Logger
import com.example.database.comparison.util.Runner
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        @Suppress("unused") // Suppressed to not bother adding later.
        private const val TAG = "CUSTOM_TAG"

        private const val NUMBER_OF_OBJECTS = 10000
        private const val NUMBER_OF_RUNS = 10
    }

    @Inject lateinit var sqliteRepo: SQLiteRepo
    @Inject lateinit var sqliteRawRepo: SQLiteRawRepo
    @Inject lateinit var sqliteBatchedRepo: SQLiteBatchedRepo
    @Inject lateinit var greenRepo: GreenRepo
    @Inject lateinit var objectboxRepo: ObjectboxRepo
    @Inject lateinit var roomRepo: RoomRepo
    @Inject lateinit var realmRepo: RealmRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getAppComponent().inject(this)
        setContentView(R.layout.activity_main)

        runTests()
    }

    private fun runTests() {
        val logger = Logger()
        val runner = Runner(logger)
        val dataProvider = DataProvider()

        val persons = dataProvider.getPersons(NUMBER_OF_OBJECTS)

        val repos = listOf(
            sqliteRepo,
            sqliteRawRepo,
            sqliteBatchedRepo,
            greenRepo,
            objectboxRepo,
            roomRepo,
            realmRepo
        )

        repos.forEach {
            Test(runner, it).run(NUMBER_OF_RUNS, persons)
        }

        Log.d(TAG, "Tests are finished")
    }
}
