package com.htec.task.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.htec.task.room.PostDatabase
import com.htec.task.di.qualifier.ActivityContext
import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import com.htec.task.retrofit.ApiCallInterface
import com.htec.task.utils.SharedUtil
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiCallInterface: ApiCallInterface,
    @ActivityContext private val context: Context,
    private val db: PostDatabase
) : IRepository {

    private val TAG = Repository::class.java.simpleName

    private var handlerException: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Coroutine exception: $throwable")
            coroutineScope.cancel("Message: ${throwable.message}", throwable)
        }

    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + handlerException)

    lateinit var userLiveData: MutableLiveData<RepositoryResult<User>>
    lateinit var postLiveData: MutableLiveData<RepositoryResult<List<Post>>>

    /**
     * Function fetch the data about posts from server and store in local cache memory.
     * @param {forceReload} mean that user always want fresh data about posts
     *
     * @see RepositoryResult
     * @return MutableLiveData with list of posts in a packed class RepositoryResult.
     */
    fun getPosts(forceReload: Boolean): MutableLiveData<RepositoryResult<List<Post>>> {
        postLiveData = MutableLiveData()

        // If local cache timed out, then retrieve posts from the server, otherwise take your data from local cache
        if (SharedUtil.isLocalCacheExpired(context) || forceReload) {
            coroutineScope.launch(Dispatchers.IO) {
                fetchRemotedPosts()
            }
        } else {
            coroutineScope.launch {
                var repositoryResult = readPostsFromLocalCache()
                postLiveData.value = repositoryResult
            }
        }

        return postLiveData
    }

    /**
     * Function which delete post from local cache.
     *
     * @param post - that user want to delete from local cache
     */
    fun deletePost(post: Post) {
        coroutineScope.launch(Dispatchers.IO + handlerException) {
            db.postDao().deletePost(post)
        }
    }

    /**
     * Function fetch the data about user who wrote some post from server and store in local cache memory.
     *
     * @see fetchUser
     * @see RepositoryResult
     * @return MutableLiveData about user in a packed class RepositoryResult.
     */
    fun getUser(userId: Long): MutableLiveData<RepositoryResult<User>> {
        userLiveData = MutableLiveData()

        coroutineScope.launch {

            val userData = withContext(Dispatchers.IO) {
                return@withContext db.userDao().loadUserData(userId)
            }

            userData?.let { _user ->
                val user = _user.user
                user.apply {
                    company = _user.company
                    address = _user.addressWithGeo.address
                    address?.let { _address ->
                        _address.geo = _user.addressWithGeo.geo
                    }
                }

                val repositoryResult: RepositoryResult<User> = RepositoryResult(user, null)
                userLiveData.value = repositoryResult

            } ?: coroutineScope.launch(Dispatchers.IO + handlerException) {
                fetchUser(userId)
            }
        }
        return userLiveData
    }

    private suspend fun fetchRemotedPosts() {
        apiCallInterface.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                val repositoryResult: RepositoryResult<List<Post>> = RepositoryResult(null, t)
                postLiveData.value = repositoryResult
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let { listOfPosts ->
                        postLiveData.value = RepositoryResult(listOfPosts, null)

                        // Suspension function can be called only within coroutine scope
                        coroutineScope.launch {
                            // New posts are saved locally and previous posts are deleted
                            storePostsLocally(listOfPosts)

                            // When the data is refreshed, the database with the stored users should also be cleaned
                            cleanOffUserData()

                            // Refresh the timer when data has been stored
                            SharedUtil.refreshCacheTime(context)
                        }
                    }
                }
            }
        })
    }

    private suspend fun storePostsLocally(list: List<Post>) {
        withContext(Dispatchers.IO + handlerException) {
            return@withContext db.postDao().updateData(list)
        }
    }

    private suspend fun readPostsFromLocalCache(): RepositoryResult<List<Post>> {
        return coroutineScope.async(Dispatchers.IO) {
            var allPosts = db.postDao().getAllPost()
            return@async RepositoryResult(allPosts, null)
        }.await()
    }

    private suspend fun cleanOffUserData() {
        withContext(Dispatchers.IO + handlerException) {
            return@withContext db.userDao().deleteAllUsers()
        }
    }

    private fun fetchUser(userId: Long) {
        apiCallInterface.getUserById(userId).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                userLiveData.value = RepositoryResult(null, t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        userLiveData.value = RepositoryResult(user, null)
                        saveUserLocally(user)
                    }
                }
            }
        })
    }

    private fun saveUserLocally(user: User) {
        coroutineScope.launch(Dispatchers.IO + handlerException) {
            db.userDao().addUser(user)
        }
    }

    private val job: Job = Job()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun cancelJob() {
        if (job.isActive) {
            Log.i(TAG, "Job is canceled!")
            job.cancel() // Once you cancel a Job, you cannot reuse it for coroutines.
        }
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }
}