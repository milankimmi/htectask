package com.htec.task.di.module

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.htec.task.room.PostDatabase
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.di.scope.ActivityScope
import com.htec.task.repository.Repository
import com.htec.task.retrofit.ApiCallInterface
import com.htec.task.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @ActivityScope
    fun provideRepository(apiCallInterface: ApiCallInterface, @ActivityContext context: Context, db : PostDatabase): Repository {
        return Repository(apiCallInterface, context, db)
    }

    @Provides
    @ActivityScope
    fun provideViewModelProviderFactory(repository: Repository, lifecycle: Lifecycle): ViewModelProvider.Factory {
        return ViewModelFactory(repository, lifecycle)
    }
}