package com.example.database.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.database.comparison.greendao.model.PersonGreenDao
import com.example.database.comparison.objectbox.model.PersonObjectbox
import com.example.database.comparison.realm.model.PersonRealm
import com.example.database.comparison.room.dao.PersonRoomDao
import com.example.database.comparison.sqlite.dao.PersonSQLiteDao
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

    @Inject
    lateinit var sqliteDao: PersonSQLiteDao
    @Inject
    lateinit var greenDao: PersonGreenDao
    @Inject
    lateinit var objectboxDao: Box<PersonObjectbox>
    @Inject
    lateinit var roomDao: PersonRoomDao
    @Inject
    lateinit var realmDao: Realm

    private val logger = Logger()
    private val runner = TestRunner(logger)
    private val dataProvider = DataProvider()

    private val persons = dataProvider.getPersons(NUMBER_OF_OBJECTS)
    private val personsSqlite = DataTransformer.toPersonsSQLite(persons)
    private val personsGreendao = DataTransformer.toPersonsGreendao(persons)
    private val personsObjectbox = DataTransformer.toPersonsObjectbox(persons)
    private val personsRoom = DataTransformer.toPersonsRoom(persons)
    private val personsRealm = DataTransformer.toPersonsRealm(persons)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getAppComponent().inject(this)
        setContentView(R.layout.activity_main)

        // Insert test.

        runner
            .beforeEach { sqliteDao.deleteAll() }
            .run("SQLite-insert", NUMBER_OF_RUNS) { sqliteDao.insertInTx(personsSqlite) }

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

        // Read test.

        runner
            .before {
                sqliteDao.deleteAll()
                sqliteDao.insertInTx(personsSqlite)
            }
            .run("SQLite-read", NUMBER_OF_RUNS) { sqliteDao.getAll() }

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
