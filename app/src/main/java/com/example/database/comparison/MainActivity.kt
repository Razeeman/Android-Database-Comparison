package com.example.database.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.database.comparison.greendao.model.DaoMaster
import com.example.database.comparison.objectbox.model.MyObjectBox
import com.example.database.comparison.objectbox.model.PersonObjectbox
import com.example.database.comparison.realm.model.PersonRealm
import com.example.database.comparison.room.db.AppRoomDatabase
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    companion object {
        @Suppress("unused") // Suppressed to not bother adding later.
        private const val TAG = "CUSTOM_TAG"

        private const val NUMBER_OF_OBJECTS = 1000
        private const val NUMBER_OF_RUNS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greenDao = DaoMaster(DaoMaster
            .DevOpenHelper(this, "greendao-database")
            .writableDb)
            .newSession()
            .personGreenDao

        val objectboxDao = MyObjectBox.builder()
            .androidContext(applicationContext)
            .build()
            .boxFor(PersonObjectbox::class.java)

        val roomDao = Room.databaseBuilder(applicationContext,
            AppRoomDatabase::class.java, "room-database")
            .allowMainThreadQueries()
            .build()
            .personRoomDao()

        Realm.init(this)
        val realmDao = Realm.getInstance(RealmConfiguration
            .Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        )

        val logger = Logger()
        val runner = TestRunner(logger)
        val dataProvider = DataProvider()

        val persons = dataProvider.getPersons(NUMBER_OF_OBJECTS)
        val personsGreendao = DataTransformer.toPersonsGreendao(persons)
        val personsObjectbox = DataTransformer.toPersonsObjectbox(persons)
        val personsRoom = DataTransformer.toPersonsRoom(persons)
        val personsRealm = DataTransformer.toPersonsRealm(persons)

        runner
            .beforeEach { greenDao.deleteAll() }
            .run("Greendao-insert", NUMBER_OF_RUNS) { greenDao.insertInTx(personsGreendao) }

        runner
            .beforeEach { objectboxDao.removeAll() }
            .run("Objectbox-insert", NUMBER_OF_RUNS) { objectboxDao.put(personsObjectbox) }

        runner
            .beforeEach { roomDao.deleteAll() }
            .run("Room-insert", NUMBER_OF_RUNS) { roomDao.insertInTx(personsRoom) }

        runner
            .beforeEach { realmDao.executeTransaction { it.delete(PersonRealm::class.java) } }
            .run("Realm-insert", NUMBER_OF_RUNS) {
                realmDao.executeTransaction { it.copyToRealm(personsRealm) }
            }

        runner
            .before {
                greenDao.deleteAll()
                greenDao.insertInTx(personsGreendao)
            }
            .run("Greendao-read", NUMBER_OF_RUNS) { greenDao.loadAll() }

        runner
            .before {
                objectboxDao.removeAll()
                objectboxDao.put(personsObjectbox)
            }
            .run("Objectbox-read", NUMBER_OF_RUNS) { objectboxDao.all }

        runner
            .before {
                roomDao.deleteAll()
                roomDao.insertInTx(personsRoom)
            }
            .run("Room-read", NUMBER_OF_RUNS) { roomDao.getAll() }

        runner
            .before {
                realmDao.executeTransaction { it.delete(PersonRealm::class.java) }
                realmDao.executeTransaction { it.copyToRealm(personsRealm) }
            }
            .run("Realm-read", NUMBER_OF_RUNS) {
                realmDao.executeTransaction { it.where(PersonRealm::class.java).findAll() }
            }

    }
}
