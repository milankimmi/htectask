package com.htec.task.di.component

import android.content.Context
import com.htec.task.MainActivity
import com.htec.task.di.module.AdapterModule
import com.htec.task.di.module.RepositoryModule
import com.htec.task.di.module.RoomModule
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = [AdapterModule::class,  RepositoryModule::class], dependencies = [ApplicationComponent::class])
interface MainActivityComponent {

    @ActivityContext
    fun getContext(): Context

    fun injectMainActivity(mainActivity: MainActivity)
}