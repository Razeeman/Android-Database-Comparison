package com.example.database.comparison.dbms.realm

import androidx.test.platform.app.InstrumentationRegistry
import com.example.database.comparison.MockData
import com.example.database.comparison.dbms.realm.model.PersonRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class RealmTest {

    private val persons = MockData.getPersonsRealm()

    companion object {

        private lateinit var dao: Realm

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            Realm.init(context)
            dao = Realm.getInstance(RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .inMemory() // In memory db.
                .build()
            )
        }
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        dao.executeTransaction { it.delete(PersonRealm::class.java) }
    }

    @Test
    fun create() {
        // With data in the database.
        dao.executeTransaction { it.insert(persons) }

        // When data is loaded.
        val reloaded = dao.where(PersonRealm::class.java).findAll()

        // Then data loaded correctly.
        assertThat(reloaded.size, `is`(persons.size))
        persons.forEach { person -> assertTrue(person.firstName in reloaded.map { it.firstName }) }
    }

    @Test
    fun update_All() {
        // With data in the database.
        var managed: List<PersonRealm> = ArrayList()
        dao.executeTransaction { managed = dao.copyToRealm(persons) }

        // When data is updated and reloaded.
        dao.executeTransaction { managed.forEach { it.age = 0 } }
        val reloaded = dao.where(PersonRealm::class.java).findAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        reloaded.forEach { person -> assertThat(person.age, `is`(0)) }
    }

    @Test
    fun update_One() {
        // With data in the database.
        var managed: List<PersonRealm> = ArrayList()
        dao.executeTransaction { managed = dao.copyToRealm(persons) }

        // When data is updated and reloaded.
        val updated = managed[0]
        val reloaded = dao.apply {
            executeTransaction { updated.age = 0 }
        }.where(PersonRealm::class.java).findAll()

        // Then data updated correctly.
        assertThat(reloaded.size, `is`(persons.size))
        assertThat(reloaded.find { it.id == updated.id }!!.age, `is`(0))
        reloaded.filter { it.id != updated.id }
            .forEach { person -> assertThat(person.age, `is`(not(0))) }
    }

    @Test
    fun delete_All() {
        // With data in the database.
        dao.executeTransaction { it.insert(persons) }

        // When data is deleted and reloaded.
        val results = dao.where(PersonRealm::class.java).findAll()
        val reloaded = dao.apply {
            executeTransaction { results.deleteAllFromRealm() }
        }.where(PersonRealm::class.java).findAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }

    @Test
    fun delete_One() {
        // With data in the database.
        dao.executeTransaction { it.insert(persons) }

        // When data is deleted and reloaded.
        val deleted = dao.where(PersonRealm::class.java).findAll()[0]
        val reloaded = dao.apply {
            executeTransaction { deleted!!.deleteFromRealm() }
        }.where(PersonRealm::class.java).findAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(persons.size - 1))
        assertFalse(deleted in reloaded)
    }

    @Test
    fun deleteAll() {
        // With data in the database.
        dao.executeTransaction { it.insert(persons) }

        // When data is deleted and reloaded.
        val reloaded = dao.apply {
            executeTransaction { it.delete(PersonRealm::class.java) }
        }.where(PersonRealm::class.java).findAll()

        // Then data deleted correctly.
        assertThat(reloaded.size, `is`(0))
    }
}