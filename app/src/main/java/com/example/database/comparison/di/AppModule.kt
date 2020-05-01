package com.example.database.comparison.di

import android.content.Context
import androidx.room.Room
import com.example.database.comparison.App
import com.example.database.comparison.dbms.greendao.DaoMaster
import com.example.database.comparison.dbms.greendao.PersonGreenDao
import com.example.database.comparison.dbms.objectbox.model.MyObjectBox
import com.example.database.comparison.dbms.objectbox.model.PersonObjectbox
import com.example.database.comparison.dbms.room.dao.PersonRoomDao
import com.example.database.comparison.dbms.room.db.AppRoomDatabase
import com.example.database.comparison.dbms.sqlite.dao.PersonSQLiteDao
import com.example.database.comparison.dbms.sqlite.db.AppSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.objectbox.Box
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

/**
 * Module for application level dependencies.
 */
@Module
class AppModule(application: App) {

    private val applicationContext = application.applicationContext

    @Provides
    @Singleton
    fun getAppContext(): Context {
        return applicationContext
    }

    @Provides
    @Singleton
    fun getPersonSQLiteDao(): PersonSQLiteDao {
        return PersonSQLiteDao.get(
            AppSQLiteDatabase.get(applicationContext))
    }

    @Provides
    @Singleton
    fun getPersonGreenDao(): PersonGreenDao {
        return DaoMaster(DaoMaster
            .DevOpenHelper(applicationContext, "greendao-database")
            .writableDb)
            .newSession()
            .personGreenDao
    }

    @Provides
    @Singleton
    fun getPersonObjectboxDao(): Box<PersonObjectbox> {
       return MyObjectBox.builder()
            .androidContext(applicationContext)
            .build()
            .boxFor(PersonObjectbox::class.java)
    }

    @Provides
    @Singleton
    fun getPersonRoomDao(): PersonRoomDao {
        return Room.databaseBuilder(applicationContext,
            AppRoomDatabase::class.java, "room-database")
            .allowMainThreadQueries()
            .build()
            .personRoomDao()
    }

    @Provides
    @Singleton
    fun getPersonRealmDao(): Realm {
        Realm.init(applicationContext)
        return Realm.getInstance(RealmConfiguration
            .Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        )
    }

}