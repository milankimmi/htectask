package com.htec.task

import android.app.Application
import android.content.Context
import com.htec.task.di.component.ApplicationComponent
import com.htec.task.di.component.DaggerApplicationComponent
import com.htec.task.di.module.ApplicationContextModule
import com.htec.task.di.module.RetrofitModule
import com.htec.task.di.qualifier.ApplicationContext
import javax.inject.Inject

class MyApplication : Application() {

    @Inject
    @ApplicationContext
    lateinit var mApplication: Context

    companion object {
        lateinit var ctx: Context
        lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        appComponent = initDaggerComponent()

        appComponent.injectApplication(this)
    }

    private fun initDaggerComponent(): ApplicationComponent {
        appComponent = DaggerApplicationComponent.builder()
            .applicationContextModule(ApplicationContextModule(this))
            .retrofitModule(RetrofitModule())
            .build()

        return appComponent
    }

    fun provideAppComponent(): ApplicationComponent {
        return appComponent
    }
}