package com.gaspardeelias.quickreddit.domain.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.kernel.model.WithEntityId


/**
 *
 *  Supports multiple headers
 *  No reflection
 *  Simple
 *
 *  Add more functionality if needed
 */

open class BaseEndlessListViewHolder2<T : WithEntityId, VS>(root: View) : RecyclerView.ViewHolder(root) {

    open fun bindCustomView(element: T, viewState : VS?) {}
    open fun bindHeaderView(viewState: VS?) {}
    open fun bindEmptyView() {}
    open fun bindLoaderView() {}
    open fun bindErrorView() {}
}

data class ItemWrapper<T : WithEntityId>(val item: T? = null, val header: Boolean = false) : WithEntityId {
    override val id: String
        get() = item?.id ?: "HEADER_ID"
}

open class BaseEndlessListAdapter2<T : WithEntityId, VS>(
    private val emptyLayout: Int = R.layout.empty_list_generic,
    private val errorLayout: Int = R.layout.list_error,
    private val loaderLayout: Int = R.layout.shimmer_item,
    protected val headerLayouts: ArrayList<Int> = ArrayList(),
    private val customLayout: Int) : RecyclerView.Adapter<BaseEndlessListViewHolder2<T, VS>>() {

    private var dataList: EndlessList<T> = EndlessList.NotReady<T>(emptyList())
    private val wrappedList: ArrayList<ItemWrapper<T>> = ArrayList()
    public var loadingEnabled = true

    var viewState : MutableMap<String, VS> = mutableMapOf()

    init {

        for (header in headerLayouts) {
            wrappedList.add(ItemWrapper(null, true))
        }
    }

    open fun shimmerEnabled() = true

    fun getCurrentItems() = dataList.list

    fun getItemAtPosition(position: Int): T {
        return wrappedList[position].item!!
    }

    fun getItemPosition(item : T) =
        wrappedList.indexOfFirst { wrapper -> wrapper.item?.id == item.id }

    fun getItemById(id: String): T? {
        return wrappedList.filter { it -> it.id == id }.getOrNull(0)?.item
    }

    fun removeItems(items: List<T>) {
        val newList = arrayListOf<T>()
        val set = items.map { it.id }.toSet()
        dataList.list.forEach {
            if (!set.contains(it.id)) {
                newList.add(it)
            }
        }
        update(EndlessList.Ready<T>(newList))
    }


    fun removeItem(item: T) {
        val newList = arrayListOf<T>()
        dataList.list.forEach {
            if (it.id != item.id) {
                newList.add(it)
            }
        }
        update(EndlessList.Ready<T>(newList))
    }

    fun updateItem(item: T) {
        val newList = arrayListOf<T>()
        dataList.list.forEach {
            if (it.id == item.id) {
                newList.add(item)
            } else newList.add(it)
        }
        update(EndlessList.Ready<T>(newList))
    }

    open fun update(newList: EndlessList<T>) {
        dataList = newList
        wrappedList.clear()

        for (header in headerLayouts) {
            wrappedList.add(ItemWrapper(null, true))
        }
        dataList.list.forEach {
            wrappedList.add(ItemWrapper(it, false))
        }
        notifyDataSetChanged()
    }

    fun isLoadingNextPage() = dataList is EndlessList.Loading
    fun isNotReady() = dataList is EndlessList.NotReady

    open fun createViewState() : VS? = null

    private fun getViewState(id : String) : VS? {
        var vs: VS? = viewState[id]
        if (vs == null) {
            vs = createViewState()?.let {
                viewState[id] = it
                it
            }
        }
        return vs
    }

    override fun onBindViewHolder(holder: BaseEndlessListViewHolder2<T, VS>, position: Int) {
        if (wrappedList.isEmpty()) return
        val viewType = getItemViewType(position)

        if (headerLayouts.contains(viewType)) {
            holder.bindHeaderView(getViewState("HEADER"))
            return
        }
        when (viewType) {
            LOADER -> holder.bindLoaderView()
            EMPTY -> holder.bindEmptyView()
            ERROR -> holder.bindErrorView()
            else -> {
                val item = wrappedList[position].item!!
                var vs : VS? = getViewState(item.id)
                holder.bindCustomView(wrappedList[position].item!!, vs)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEndlessListViewHolder2<T, VS> =
        if (headerLayouts.contains(viewType)) {
            createHeaderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, null), headerLayouts.indexOf(viewType))
        } else {
            when (viewType) {
                LOADER -> createLoaderViewHolder(LayoutInflater.from(parent.context).inflate(loaderLayout, null))
                EMPTY -> createEmptyViewHolder(LayoutInflater.from(parent.context).inflate(emptyLayout, parent, false))
                ERROR -> createErrorViewHolder(LayoutInflater.from(parent.context).inflate(errorLayout, null))
                else -> createCustomViewHolder(inflateCustomItemLayout(parent.context, viewType))
            }
        }

    open fun createHeaderViewHolder(view: View, index: Int) = BaseEndlessListViewHolder2<T, VS>(view)
    open fun createCustomViewHolder(view: View) = BaseEndlessListViewHolder2<T, VS>(view)
    open fun createLoaderViewHolder(view: View) = BaseEndlessListViewHolder2<T, VS>(view)
    open fun createEmptyViewHolder(view: View) = BaseEndlessListViewHolder2<T, VS>(view)
    open fun createErrorViewHolder(view: View) = BaseEndlessListViewHolder2<T, VS>(view)

    open fun getCustomItemViewType(position: Int) = customLayout
    open fun inflateCustomItemLayout(context: Context, viewType: Int) = LayoutInflater.from(context).inflate(customLayout, null)


    override fun getItemViewType(position: Int): Int =
        if (position < headerLayouts.size) {
            headerLayouts[position]
        } else if (dataList.list.isEmpty() && dataList is EndlessList.Ready) {
            EMPTY
        } else {
            when (dataList) {
                is EndlessList.NotReady -> LOADER
                is EndlessList.Loading -> if (position < wrappedList.size) getCustomItemViewType(position) else LOADER
                is EndlessList.Error -> ERROR
                is EndlessList.Ready -> getCustomItemViewType(position)
            }
        }

    override fun getItemCount(): Int =
        when (dataList) {
            is EndlessList.NotReady -> if (loadingEnabled && shimmerEnabled()) EMPTY_LOADERS_COUNT else 0
            is EndlessList.Loading -> wrappedList.size + LOAD_MORE_LOADERS_COUNT
            is EndlessList.Error -> 1
            is EndlessList.Ready ->  if(wrappedList.size>0) wrappedList.size else 1
        }


    companion object {
        const val EMPTY = 1
        const val ERROR = 2
        const val LOADER = 3
        const val CUSTOM_VIEW = 4
        const val EMPTY_LOADERS_COUNT = 10
        const val LOAD_MORE_LOADERS_COUNT = 1
    }

}