package com.htec.task.utils

import java.util.concurrent.TimeUnit

class TimeMock : ITime {

    private var currentTimeMillis: Long = 0

    init {
        currentTimeMillis = System.currentTimeMillis()
    }

    fun timePasses(minutes: Long): Long {
        val minutesInMills = TimeUnit.MINUTES.toMillis(minutes) + 1000
        currentTimeMillis += minutesInMills
        return currentTimeMillis
    }

    fun setCurrentTimeMillis(currentTime: Long) {
        currentTimeMillis = currentTime
    }

    override fun getCurrentTimeMillis(): Long {
        return currentTimeMillis
    }
}