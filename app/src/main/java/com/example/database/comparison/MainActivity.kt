package com.example.database.comparison

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.database.comparison.dbms.greendao.PersonGreenDao
import com.example.database.comparison.dbms.objectbox.PersonObjectbox
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.test.*
import com.example.database.comparison.util.DataProvider
import com.example.database.comparison.util.Logger
import com.example.database.comparison.util.Runner
import io.objectbox.Box
import io.realm.Realm
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        @Suppress("unused") // Suppressed to not bother adding later.
        private const val TAG = "CUSTOM_TAG"

        private const val NUMBER_OF_OBJECTS = 10000
        private const val NUMBER_OF_RUNS = 10
    }

    @Inject lateinit var sqliteDao: PersonSQLiteDao
    @Inject lateinit var greenDao: PersonGreenDao
    @Inject lateinit var objectboxDao: Box<PersonObjectbox>
    @Inject lateinit var roomDao: PersonRoomDao
    @Inject lateinit var realmDao: Realm

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

        TestSQLite(runner, sqliteDao).run(NUMBER_OF_RUNS, persons)
        TestSQLiteRaw(runner, sqliteDao).run(NUMBER_OF_RUNS, persons)
        TestGreendao(runner, greenDao).run(NUMBER_OF_RUNS, persons)
        TestObjectbox(runner, objectboxDao).run(NUMBER_OF_RUNS, persons)
        TestRoom(runner, roomDao).run(NUMBER_OF_RUNS, persons)
        TestRealm(runner, realmDao).run(NUMBER_OF_RUNS, persons)

        Log.d(TAG, "Tests are finished")
    }
}
