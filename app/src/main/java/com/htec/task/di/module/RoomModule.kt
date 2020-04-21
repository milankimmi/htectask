package com.htec.task.di.module

import android.content.Context
import com.htec.task.room.PostDatabase
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.di.qualifier.ApplicationContext
import com.htec.task.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun providePostDatabase(@ApplicationContext context: Context): PostDatabase {
        return PostDatabase.getInstance(context)!!
    }
}