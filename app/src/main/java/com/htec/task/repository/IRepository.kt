package com.htec.task.repository

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

interface IRepository : LifecycleObserver {
    fun registerLifecycle(lifecycle: Lifecycle)
}