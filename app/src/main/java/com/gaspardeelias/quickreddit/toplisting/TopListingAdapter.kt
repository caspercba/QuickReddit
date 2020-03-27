package com.gaspardeelias.quickreddit.toplisting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.domain.common.BaseEndlessListAdapter2
import com.gaspardeelias.quickreddit.domain.common.BaseEndlessListViewHolder2
import com.gaspardeelias.quickreddit.domain.common.BaseRecyclerViewAdapter

class TopListingAdapter: BaseEndlessListAdapter2<TopListingElement, Nothing>(
    emptyLayout = R.layout.empty_list_generic,
    customLayout = R.layout.item_list_content,
    loaderLayout = R.layout.loading
) {

    override fun shimmerEnabled() = true

    override fun inflateCustomItemLayout(context: Context, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, null)
    }

    override fun createCustomViewHolder(view: View): BaseEndlessListViewHolder2<TopListingElement, Nothing> {
        return TopListingVH(view)
    }
}

class TopListingVH(val rootView: View): BaseEndlessListViewHolder2<TopListingElement, Nothing>(rootView) {
    private val author by lazy { itemView.findViewById<TextView>(R.id.id_author)}

    override fun bindCustomView(element: TopListingElement, viewState: Nothing?) {
        author?.text = element.authorFullname
    }

}