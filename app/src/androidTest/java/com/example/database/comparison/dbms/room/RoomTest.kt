package com.example.database.comparison.dbms.room

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.database.comparison.MockData
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.db.AppRoomDatabase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class RoomTest {

    private lateinit var dao: PersonRoomDao
    private val persons = MockData.getPersonsRoom()

    companion object {

        private lateinit var db: AppRoomDatabase

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            // In memory database.
            db = Room.inMemoryDatabaseBuilder(context,
                AppRoomDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }
    }

    @Before
    fun setUp() {
        dao = db.personRoomDao()
    }

    @After
    fun tearDown() {
        dao.deleteAll()
    }

    @Test
    fun create() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is loaded.
        val reloaded = dao.getAll()

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person in reloaded) }
    }

    @Test
    fun update_All() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is updated and reloaded.
        val reloaded = dao.apply {
            updateInTx(getAll().apply { forEach { it.age = 0 } })
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_One() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is updated and reloaded.
        val updated = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            updateInTx(listOf(updated))
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        assertThat(reloaded.find { it.id == updated.id }!!.age, `is`(0))
        reloaded.filter { it.id != updated.id }
            .forEach { person -> assertThat(person.age, `is`(not(0))) }
    }

    @Test
    fun delete_All() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            deleteInTx(getAll())
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_One() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is deleted and reloaded.
        val deleted = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            deleteInTx(listOf(deleted))
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(persons.size - 1))
        assertFalse(deleted in reloaded)
    }

    @Test
    fun deleteAll() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            deleteAll()
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }
}