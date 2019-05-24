package com.example.database.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.database.comparison.greendao.model.DaoMaster
import com.example.database.comparison.room.db.AppRoomDatabase

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

        val greenDao = DaoMaster(DaoMaster
            .DevOpenHelper(this, "greendao-database")
            .writableDb)
            .newSession()
            .personDao

        val logger = Logger()
        val runner = TestRunner(logger)
        val dataProvider = DataProvider()

        val persons = dataProvider.getPersons(1000)
        val roomPersons = DataTransformers.toRoomPersons(persons)
        val greendaoPersons = DataTransformers.toGreendaoPersons(persons)

        roomDao.deleteAll()
        runner.run("Room-insert", 10) { roomDao.insertInTx(roomPersons) }

        greenDao.deleteAll()
        runner.run("Greendao-insert", 10) { greenDao.insertInTx(greendaoPersons) }

    }
}
