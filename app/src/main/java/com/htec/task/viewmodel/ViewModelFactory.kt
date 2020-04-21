package com.htec.task.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.htec.task.repository.Repository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    val repository: Repository,
    val lifecycle: Lifecycle
) : ViewModelProvider.Factory {

    // Lifecycle is a class that holds the information about the lifecycle state of a component
    // (like an activity or a fragment) and allows other objects to observe this state.
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            repository.registerLifecycle(lifecycle)
            return PostViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            repository.registerLifecycle(lifecycle)
            return UserViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}