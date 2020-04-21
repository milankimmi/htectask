package com.htec.task.utils

import com.google.gson.Gson
import com.htec.task.datamodel.Post
import java.io.File

object TestParseUtils {

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }

    fun getPostTestObject(path: String): Array<Post> {
        val gson = Gson()
        val testModel =
            gson.fromJson<Array<Post>>(getJson(path), Array<Post>::class.java)
        return testModel
    }
}