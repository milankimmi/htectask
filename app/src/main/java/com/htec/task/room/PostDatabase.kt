package com.htec.task.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.htec.task.datamodel.*
import com.htec.task.room.dao.PostsDao
import com.htec.task.room.dao.UserDao

@Database(
    entities = [User::class, Post::class, Address::class, Geo::class, Company::class],
    version = 1
)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostsDao
    abstract fun userDao(): UserDao

    companion object {
        var DATABASE_NAME: String = "post_db"
        var INSTANCE: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase? {
            if (INSTANCE == null) {
                synchronized(PostDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        PostDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }

        /**
         * If we  need to add default data to your database, right after it was created or when the
         * database is opened then use RoomDatabase#Callback! Call the addCallback method when building
         * your RoomDatabase and override either onCreate or onOpen.
         */
        private var sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.i("sRoomDatabaseCallback:", "onOpen()")
                if (INSTANCE != null) {

                }
            }
        }
    }
}