package com.htec.task.di.module

import com.htec.task.MainActivity
import com.htec.task.adapter.PostRecycleViewAdapter
import com.htec.task.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityContextModule::class])
class AdapterModule {

    @Provides
    @ActivityScope
    fun providePostRecyclerViewAdapter(clickListener: PostRecycleViewAdapter.PostClickListener): PostRecycleViewAdapter {
        return PostRecycleViewAdapter(clickListener)
    }

    @Provides
    @ActivityScope
    fun provideRecycleClickListener(mainActivity: MainActivity): PostRecycleViewAdapter.PostClickListener {
        return mainActivity
    }
}