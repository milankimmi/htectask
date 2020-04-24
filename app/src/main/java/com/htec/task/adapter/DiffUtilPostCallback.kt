package com.htec.task.adapter

import androidx.recyclerview.widget.DiffUtil
import com.htec.task.datamodel.Post

class DiffUtilPostCallback(
    private val oldList: List<Post>,
    private val newList: List<Post>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition] == newList[newItemPosition])
    }
}