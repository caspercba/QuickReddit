package com.gaspardeelias.quickreddit.toplisting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.repo.model.TopListingElementDto
import org.jetbrains.anko.onClick

class PostViewHolder(view: View, val onClick: (element: TopListingElementDto, action: Int) -> Unit): RecyclerView.ViewHolder(view) {

    private val author by lazy { itemView.findViewById<TextView>(R.id.id_author) }
    private val title by lazy { itemView.findViewById<TextView>(R.id.id_title) }
    private val date by lazy { itemView.findViewById<TextView>(R.id.id_date) }
    private val cardview by lazy { itemView.findViewById<ImageView>(R.id.id_cardview) }
    private val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.id_thumbnail) }
    private val dot by lazy { itemView.findViewById<ImageView>(R.id.id_dot) }

    init {
    }

    fun bind(post: TopListingElementDto?) {
        author.text = post?.author
        title.text = post?.title
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (element: TopListingElementDto, action: Int) -> Unit): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return PostViewHolder(view, onClick)
        }
    }

}