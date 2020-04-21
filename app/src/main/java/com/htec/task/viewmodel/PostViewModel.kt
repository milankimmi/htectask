package com.htec.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import com.htec.task.repository.Repository
import com.htec.task.repository.RepositoryResult

class PostViewModel(private val repository: Repository) : ViewModel() {

    private var post: MutableLiveData<RepositoryResult<List<Post>>>? = null
    private var user: MutableLiveData<RepositoryResult<User>>? = null

    fun getPosts(forceReload: Boolean = false): MutableLiveData<RepositoryResult<List<Post>>> {
        post = repository.getPosts(forceReload)
        return post!!
    }

    fun deletePost(post: Post) {
        repository.deletePost(post)
    }
}