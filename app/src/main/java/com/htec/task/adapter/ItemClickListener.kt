package com.htec.task.adapter

import com.htec.task.datamodel.Post

interface ItemClickListener {
    fun onPostItemClicked(position: Int, post: Post)
}