package com.example.database.comparison.di

import com.example.database.comparison.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Component for application level dependencies.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

}