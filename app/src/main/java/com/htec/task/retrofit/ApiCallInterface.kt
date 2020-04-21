package com.htec.task.retrofit

import com.htec.task.datamodel.Post
import com.htec.task.datamodel.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiCallInterface {

    @GET("/posts")
    fun getPosts(): Call<List<Post>>

    @GET("/posts")
    fun getPosts(@Url url: String): Call<List<Post>>

    @GET("/users/{userId}")
    fun getUserById(@Path("userId") orderId: Long): Call<User>
}