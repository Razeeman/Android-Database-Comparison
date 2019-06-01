package com.example.database.comparison.dbms.sqlite

import android.database.sqlite.SQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.example.database.comparison.MockData
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.db.AppSQLiteDatabase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class SQLiteDaoTest {

    private lateinit var dao: PersonSQLiteDao
    private val persons = MockData.getPersonsSQLite()

    companion object {

        private lateinit var db: SQLiteDatabase

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            db = AppSQLiteDatabase.get(context)
        }
    }

    @Before
    fun setUp() {
        dao =
            PersonSQLiteDao.get(db)
    }

    @After
    fun tearDown() {
        dao.deleteAll()
    }

    @Test
    fun create_InTx() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is loaded.
        val reloaded = dao.getAll()

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person in reloaded) }
    }

    @Test
    fun create_Raw() {
        // With data in the database.
        dao.insertRaw(persons)

        // When data is loaded.
        val reloaded = dao.getAll()

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person in reloaded) }
    }

    @Test
    fun create_Batched() {
        // With data in the database.
        dao.insertBatched(persons)

        // When data is loaded.
        val reloaded = dao.getAll()

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person in reloaded) }
    }

    @Test
    fun update_InTx_All() {
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
    fun update_InTx_One() {
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
    fun update_Raw_All() {
        // With data in the database.
        dao.insertRaw(persons)

        // When data is updated and reloaded.
        val reloaded = dao.apply {
            updateRaw(getAll().apply { forEach { it.age = 0 } })
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_Raw_One() {
        // With data in the database.
        dao.insertRaw(persons)

        // When data is updated and reloaded.
        val updated = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            updateRaw(listOf(updated))
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        assertThat(reloaded.find { it.id == updated.id }!!.age, `is`(0))
        reloaded.filter { it.id != updated.id }
            .forEach { person -> assertThat(person.age, `is`(not(0))) }
    }

    @Test
    fun update_Batched_All() {
        // With data in the database.
        dao.insertBatched(persons)

        // When data is updated and reloaded.
        val reloaded = dao.apply {
            updateBatched(getAll().apply { forEach { it.age = 0 } })
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_Batched_One() {
        // With data in the database.
        dao.insertBatched(persons)

        // When data is updated and reloaded.
        val updated = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            updateBatched(listOf(updated))
        }.getAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        assertThat(reloaded.find { it.id == updated.id }!!.age, `is`(0))
        reloaded.filter { it.id != updated.id }
            .forEach { person -> assertThat(person.age, `is`(not(0))) }
    }

    @Test
    fun delete_InTx_All() {
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
    fun delete_InTx_One() {
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
    fun delete_Raw_All() {
        // With data in the database.
        dao.insertRaw(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            deleteRaw(getAll())
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_Raw_One() {
        // With data in the database.
        dao.insertRaw(persons)

        // When data is deleted and reloaded.
        val deleted = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            deleteRaw(listOf(deleted))
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(persons.size - 1))
        assertFalse(deleted in reloaded)
    }

    @Test
    fun delete_Batched_All() {
        // With data in the database.
        dao.insertBatched(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            deleteBatched(getAll())
        }.getAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_Batched_One() {
        // With data in the database.
        dao.insertBatched(persons)

        // When data is deleted and reloaded.
        val deleted = dao.getAll()[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            deleteBatched(listOf(deleted))
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