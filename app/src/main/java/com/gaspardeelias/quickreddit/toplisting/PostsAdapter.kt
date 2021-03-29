package com.gaspardeelias.quickreddit.toplisting

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gaspardeelias.repo.model.Post


class PostsAdapter(val onClick: (element: Post, action: Int) -> Unit)
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


/*class TopListingAdapter(val onClick: (element: TopListingElement, action: Int) -> Unit) :
    BaseEndlessListAdapter2<TopListingElement, Nothing>(
        emptyLayout = R.layout.empty_list_generic,
        customLayout = R.layout.item_list_content,
        loaderLayout = R.layout.loading
    ) {

    companion object {
        val ACTION_SEE_DETAILS = 0
        val ACTION_DISMISS_ITEM = 1
    }


    override fun shimmerEnabled() = false

    override fun inflateCustomItemLayout(context: Context, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, null)
    }

    override fun createCustomViewHolder(view: View): BaseEndlessListViewHolder2<TopListingElement, Nothing> {
        return TopListingVH(view, onClick)
    }
}

class TopListingVH(val rootView: View, val onClick: (element: TopListingElement, action: Int) -> Unit) :
    BaseEndlessListViewHolder2<TopListingElement, Nothing>(rootView) {
    private val author by lazy { itemView.findViewById<TextView>(R.id.id_author) }
    private val title by lazy { itemView.findViewById<TextView>(R.id.id_title) }
    private val date by lazy { itemView.findViewById<TextView>(R.id.id_date) }
    private val cardview by lazy { itemView.findViewById<ImageView>(R.id.id_cardview) }
    private val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.id_thumbnail) }
    private val dot by lazy { itemView.findViewById<ImageView>(R.id.id_dot) }
    private val dismissButton by lazy { itemView.findViewById<View>(R.id.dismiss_container) }


    override fun bindCustomView(element: TopListingElement, viewState: Nothing?) {
        author?.text = element.author
        title?.text = element.title
        loadCroppedImage(thumbnail, element.thumbnail)
        rootView.setOnClickListener { onClick(element, ACTION_SEE_DETAILS) }
        if (element.viewed) {
            dot?.visibility = View.GONE
        } else {
            dot?.visibility = View.VISIBLE
        }
        element.createdUtc?.let { date?.text = DateHelper.getTimeAgo(rootView.context, it * 1000L)}
        dismissButton?.onClick {
            onClick(element, ACTION_DISMISS_ITEM)
        }
    }

}*/
