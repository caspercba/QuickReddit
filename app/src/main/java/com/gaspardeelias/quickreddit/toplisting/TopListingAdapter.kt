package com.gaspardeelias.quickreddit.toplisting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.domain.common.BaseEndlessListAdapter2
import com.gaspardeelias.quickreddit.domain.common.BaseEndlessListViewHolder2
import com.gaspardeelias.quickreddit.utils.loadCroppedImage

class TopListingAdapter(val onClick: (element: TopListingElement) -> Unit) :
    BaseEndlessListAdapter2<TopListingElement, Nothing>(
        emptyLayout = R.layout.empty_list_generic,
        customLayout = R.layout.item_list_content,
        loaderLayout = R.layout.loading
    ) {

    override fun shimmerEnabled() = true

    override fun inflateCustomItemLayout(context: Context, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, null)
    }

    override fun createCustomViewHolder(view: View): BaseEndlessListViewHolder2<TopListingElement, Nothing> {
        return TopListingVH(view, onClick)
    }
}

class TopListingVH(val rootView: View, val onClick: (element: TopListingElement) -> Unit) :
    BaseEndlessListViewHolder2<TopListingElement, Nothing>(rootView) {
    private val author by lazy { itemView.findViewById<TextView>(R.id.id_author) }
    private val title by lazy { itemView.findViewById<TextView>(R.id.id_title) }
    private val cardview by lazy { itemView.findViewById<ImageView>(R.id.id_cardview) }
    private val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.id_thumbnail) }
    private val dot by lazy { itemView.findViewById<ImageView>(R.id.id_dot) }


    override fun bindCustomView(element: TopListingElement, viewState: Nothing?) {
        author?.text = element.authorFullname
        title?.text = element.title
        loadCroppedImage(thumbnail, element.thumbnail)
        rootView.setOnClickListener { onClick(element) }
        if (element.viewed) {
            dot?.visibility = View.GONE
        } else {
            dot?.visibility = View.VISIBLE
        }
    }

}
