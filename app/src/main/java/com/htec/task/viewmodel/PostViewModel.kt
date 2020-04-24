package com.htec.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import com.htec.task.repository.Repository
import com.htec.task.repository.RepositoryResult

class PostViewModel(private val repository: Repository) : ViewModel() {

    private var posts: MutableLiveData<RepositoryResult<List<Post>>>? = null

    fun getPosts(forceReload: Boolean = false): MutableLiveData<RepositoryResult<List<Post>>> {
        if (posts == null || forceReload || repository.isCacheExpired()) {
            posts = repository.getPosts(forceReload)
        }
        return posts!!
    }

    fun deletePost(post: Post) {
        var repositoryResult = posts?.value
        if (repositoryResult?.hasResult()!!) {
            var postList = repositoryResult.success
            var newResult = postList?.filterIndexed { index, element ->
                post.id != element.id
            }
            posts?.value = RepositoryResult(newResult, null)
        }

        repository.deletePost(post)
    }
}
