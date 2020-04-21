package com.htec.task.di.module

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.htec.task.MainActivity
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityContextModule(val context: Context, val mainActivity: MainActivity) {

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @ActivityScope
    fun provideMainActivity(): MainActivity {
        return mainActivity
    }

    @Provides
    @ActivityScope
    fun provideRecycleClickListener(mainActivity: MainActivity): Lifecycle {
        return mainActivity.lifecycle
    }

}