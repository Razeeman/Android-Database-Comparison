package com.example.database.comparison.dbms.objectbox

import androidx.test.platform.app.InstrumentationRegistry
import com.example.database.comparison.MockData
import io.objectbox.Box
import io.objectbox.BoxStore
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ObjectBoxTest {

    private val persons = MockData.getPersonsObjectbox()
    private lateinit var dao: Box<PersonObjectbox>

    companion object {

        private lateinit var db: BoxStore

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            db = MyObjectBox.builder()
                .androidContext(context)
                .build()
        }
    }

    @Before
    fun setUp() {
        dao = db.boxFor(PersonObjectbox::class.java)
    }

    @After
    fun tearDown() {
        dao.removeAll()
    }

    @Test
    fun create() {
        // With data in the database.
        dao.put(persons)

        // When data is loaded.
        val reloaded = dao.all

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person in reloaded) }
    }

    @Test
    fun update_All() {
        // With data in the database.
        dao.put(persons)

        // When data is updated and reloaded.
        val reloaded = dao.apply {
            put(all.apply { forEach { it.age = 0 } })
        }.all

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_One() {
        // With data in the database.
        dao.put(persons)

        // When data is updated and reloaded.
        val updated = dao.all[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            put(listOf(updated))
        }.all

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        assertThat(reloaded.find { it.id == updated.id }!!.age, `is`(0))
        reloaded.filter { it.id != updated.id }
            .forEach { person -> assertThat(person.age, `is`(not(0))) }
    }

    @Test
    fun delete_All() {
        // With data in the database.
        dao.put(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            remove(all)
        }.all

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_One() {
        // With data in the database.
        dao.put(persons)

        // When data is deleted and reloaded.
        val deleted = dao.all[0]
            .apply { age = 0 }
        val reloaded = dao.apply {
            remove(listOf(deleted))
        }.all

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(persons.size - 1))
        assertFalse(deleted in reloaded)
    }

    @Test
    fun deleteAll() {
        // With data in the database.
        dao.put(persons)

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            removeAll()
        }.all

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }
}