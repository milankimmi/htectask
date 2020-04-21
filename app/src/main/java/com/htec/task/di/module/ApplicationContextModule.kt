package com.htec.task.di.module

import android.app.Application
import android.content.Context
import com.htec.task.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationContextModule(private val application: Application) {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return application
    }
}