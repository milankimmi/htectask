package com.htec.task.di.component

import android.content.Context
import com.htec.task.MyApplication
import com.htec.task.di.module.ApplicationContextModule
import com.htec.task.di.module.RetrofitModule
import com.htec.task.di.module.RoomModule
import com.htec.task.di.qualifier.ApplicationContext
import com.htec.task.retrofit.ApiCallInterface
import com.htec.task.room.PostDatabase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationContextModule::class, RetrofitModule::class, RoomModule::class])
interface ApplicationComponent {

    fun provideRoomDataBase(): PostDatabase

    fun provideApiCallInterface(): ApiCallInterface

    @ApplicationContext
    fun provideApplication(): Context

    fun injectApplication(myApplication: MyApplication)
}