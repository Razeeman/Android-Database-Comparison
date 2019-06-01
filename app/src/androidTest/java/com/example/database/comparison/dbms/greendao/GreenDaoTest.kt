package com.example.database.comparison.dbms.greendao

import androidx.test.platform.app.InstrumentationRegistry
import com.example.database.comparison.MockData
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class GreenDaoTest {

    private lateinit var dao: PersonGreenDao
    private val persons = MockData.getPersonsGreendao()

    companion object {

        private lateinit var db: DaoMaster

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            db = DaoMaster(DaoMaster
                .DevOpenHelper(context, "greendao-database")
                .writableDb)
        }
    }

    @Before
    fun setUp() {
        dao = db.newSession().personGreenDao
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
        val reloaded = dao.loadAll()

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
            updateInTx(loadAll().apply { forEach { it.age = 0 } })
        }.loadAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_One() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is updated and reloaded.
        val updated = dao.loadAll()[0]
                .apply { age = 0 }
        val reloaded = dao.apply {
            updateInTx(listOf(updated))
        }.loadAll()

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
            deleteInTx(loadAll())
        }.loadAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_One() {
        // With data in the database.
        dao.insertInTx(persons)

        // When data is deleted and reloaded.
        val deleted = dao.loadAll()[0]
                .apply { age = 0 }
        val reloaded = dao.apply {
            deleteInTx(listOf(deleted))
        }.loadAll()

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
        }.loadAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }
}