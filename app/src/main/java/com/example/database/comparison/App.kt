package com.example.database.comparison

import android.app.Application
import com.example.database.comparison.di.AppComponent
import com.example.database.comparison.di.AppModule
import com.example.database.comparison.di.DaggerAppComponent

class App : Application() {

    companion object {

        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}