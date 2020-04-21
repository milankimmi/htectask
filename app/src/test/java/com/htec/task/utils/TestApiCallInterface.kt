package com.htec.task.utils

import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface TestApiCallInterface {

    @GET("/posts")
    fun getPosts(): Single<List<Post>>

    @GET("/posts")
    fun getPosts(@Url url: String): Single<List<Post>>

    @GET("/users/{userId}")
    fun getUserById(@Path("userId") orderId: Long): Single<User>
}