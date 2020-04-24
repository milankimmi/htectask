package com.htec.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.htec.task.databinding.PostRecycleViewItemBinding
import com.htec.task.datamodel.Post
import javax.inject.Inject

class PostRecycleViewAdapter @Inject constructor(private val listener: PostClickListener) :
    RecyclerView.Adapter<PostRecycleViewAdapter.ViewHolder>(), ItemClickListener {

    var isClickable: Boolean = true
        set(value) {
            field = value
        }

    private var dataSet: ArrayList<Post> = ArrayList()

    interface PostClickListener {
        fun onPostClicked(position: Int, post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostRecycleViewItemBinding.inflate(inflater, parent, false)

        binding.itemClickListener = this

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(dataSet[position])

    internal fun getDataAtPosition(position: Int): Post? {
        if (position >= 0 && position < dataSet.size) {
            return dataSet[position]
        }
        return null
    }

    internal fun setData(postList: ArrayList<Post>) {
        val oldList = dataSet
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilPostCallback(
                    oldList,
                    postList
                )
            )

        dataSet = postList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onPostItemClicked(position: Int, post: Post) {
        if (isClickable) {
            listener.onPostClicked(position, post)
        }
    }

    fun deletePostLocally(adapterPosition: Int) {
        if (adapterPosition >= 0 && (adapterPosition < dataSet.size)) {
            dataSet.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, dataSet.size)
        }
    }

    inner class ViewHolder(val bindingItem: PostRecycleViewItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {

        fun bind(postItem: Post) {
            bindingItem.position = adapterPosition
            bindingItem.postItem = postItem
            bindingItem.executePendingBindings()
        }
    }
}
