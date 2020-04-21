package com.htec.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.htec.task.datamodel.User
import com.htec.task.repository.Repository
import com.htec.task.repository.RepositoryResult

class UserViewModel(private val repository: Repository) : ViewModel() {

    private var user: MutableLiveData<RepositoryResult<User>>? = null

    fun getUser(userId: Long): MutableLiveData<RepositoryResult<User>> {
        user = repository.getUser(userId)
        return user!!
    }
}