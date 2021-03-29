package com.gaspardeelias.quickreddit.toplisting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.repo.model.Post
import kotlinx.android.synthetic.main.item_list_content.view.*
import org.jetbrains.anko.onClick

class PostViewHolder(val view: View, val onClick: (element: Post) -> Unit): RecyclerView.ViewHolder(view) {

    private val author by lazy { itemView.findViewById<TextView>(R.id.id_author) }
    private val title by lazy { itemView.findViewById<TextView>(R.id.id_title) }
    private val date by lazy { itemView.findViewById<TextView>(R.id.id_date) }
    private val cardview by lazy { itemView.findViewById<ImageView>(R.id.id_cardview) }
    private val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.id_thumbnail) }
    private val dot by lazy { itemView.findViewById<ImageView>(R.id.id_dot) }

    init {
    }

    fun bind(post: Post?) {
        author.text = post?.author
        title.text = post?.title

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_warning)

        Glide.with(view.context).load(post?.thumbnail).apply(options)
            .into(view.id_thumbnail)
        view.onClick { post?.let(onClick) }
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (element: Post) -> Unit): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return PostViewHolder(view, onClick)
        }
    }

}