package com.htec.task.repository

data class RepositoryResult<T>(var success: T?, var error: Throwable?) {
    fun hasResult(): Boolean {
        return success != null
    }
}