package com.htec.task.room.dao

import androidx.room.*
import com.htec.task.datamodel.Post

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post)

    @Insert
    fun insertAll(posts: List<Post>)

    @Transaction
    fun updateData(posts: List<Post>) {
        deleteAllPosts()
        insertAll(posts)
    }

    @Delete
    fun deletePost(post: Post)

    @Query("SELECT * FROM post")
    fun getAllPost(): List<Post>

    @Query("DELETE FROM post")
    fun deleteAllPosts()
}