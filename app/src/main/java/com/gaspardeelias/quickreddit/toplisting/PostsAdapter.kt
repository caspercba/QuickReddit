package com.gaspardeelias.quickreddit.toplisting

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gaspardeelias.repo.model.Post


class PostsAdapter(val onClick: (element: Post) -> Unit)
    : PagingDataAdapter<Post, PostViewHolder>(COMPARATOR) {


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.create(parent, onClick)
    }



    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.title == newItem.title

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.title == newItem.title

            override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
                return null
            }
        }
    }


}