package com.example.database.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.database.comparison.greendao.model.DaoMaster
import com.example.database.comparison.realm.model.PersonRealm
import com.example.database.comparison.room.db.AppRoomDatabase
import io.realm.Realm
import io.realm.RealmConfiguration

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
            .personRoomDao()

        val greenDao = DaoMaster(DaoMaster
            .DevOpenHelper(this, "greendao-database")
            .writableDb)
            .newSession()
            .personGreenDao

        Realm.init(this)
        val realm = Realm.getInstance(RealmConfiguration
            .Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        )

        val logger = Logger()
        val runner = TestRunner(logger)
        val dataProvider = DataProvider()

        val persons = dataProvider.getPersons(1000)
        val personsRoom = DataTransformer.toPersonsRoom(persons)
        val personsGreendao = DataTransformer.toPersonsGreendao(persons)
        val personsRealm = DataTransformer.toPersonsRealm(persons)

        runner
            .beforeEach { greenDao.deleteAll() }
            .run("Greendao-insert", runs = 10) { greenDao.insertInTx(personsGreendao) }

        runner
            .beforeEach { roomDao.deleteAll() }
            .run("Room-insert", runs = 10) { roomDao.insertInTx(personsRoom) }

        runner
            .beforeEach { realm.executeTransaction { it.delete(PersonRealm::class.java) } }
            .run("Realm-insert", runs = 10) {
                realm.executeTransaction { it.copyToRealm(personsRealm) }
            }

        runner
            .before {
                greenDao.deleteAll()
                greenDao.insertInTx(personsGreendao)
            }
            .run("Greendao-read", runs = 10) { greenDao.loadAll() }

        runner
            .before {
                roomDao.deleteAll()
                roomDao.insertInTx(personsRoom)
            }
            .run("Room-read", runs = 10) { roomDao.getAll() }

        runner
            .before {
                realm.executeTransaction { it.delete(PersonRealm::class.java) }
                realm.executeTransaction { it.copyToRealm(personsRealm) }
            }
            .run("Realm-read", runs = 10) {
                realm.executeTransaction { it.where(PersonRealm::class.java).findAll() }
            }

    }
}
