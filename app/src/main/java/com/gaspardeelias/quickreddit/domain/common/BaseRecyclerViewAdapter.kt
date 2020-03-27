package com.gaspardeelias.quickreddit.domain.common


import androidx.recyclerview.widget.RecyclerView
import com.gaspardeelias.quickreddit.model.WithId
import rx.Subscription



abstract class BaseRecyclerViewAdapter<T : WithId> :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    interface OnItemClickCallback<T> {
        fun onItemClick(item: T?)
    }

    private val items: MutableList<T>? = ArrayList()
    protected var listener: OnItemClickCallback<T?>? =
        null
    protected var subscriptions: MutableSet<Subscription?>? =
        HashSet<Subscription?>()

    fun setOnItemClickListener(callback: OnItemClickCallback<T?>?) {
        listener = callback
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (!isFooter(position)) {
            holder!!.itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onItemClick(items!![position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    fun getItems(): MutableList<T>? {
        return items
    }

    val itemsWithoutHeaderAndFooter: MutableList<T>?
        get() {
            val result: MutableList<T> = ArrayList()
            for (withId in items!!) {
                if (withId.id != ITEM_VIEW_TYPE_FOOTER && withId.id != ITEM_VIEW_TYPE_HEADER) {
                    result.add(withId)
                }
            }
            return result
        }

    fun setItems(data: MutableList<T>?) {
        val hasHeader =
            items!!.size > 0 && items[0].id == BaseRecyclerViewAdapter.Companion.ITEM_VIEW_TYPE_HEADER
        if (hasHeader) {
            val header = items[0]
            items.clear()
            items.add(header)
        } else {
            items.clear()
        }
        items.addAll(data!!)
        notifyDataSetChanged()
    }

    fun addItems(items: MutableList<T>?) {
        val size = this.items!!.size
        val ids = HashSet<Long?>()
        for (withId in this.items) {
            ids.add(withId.id)
        }
        for (withId in items!!) {
            if (!ids.contains(withId.id)) {
                this.items.add(withId)
            }
        }
        notifyItemRangeInserted(size, this.items.size)
    }

    fun addItemsToBeggining(items: MutableList<T>?) {
        val size = this.items!!.size
        val ids = HashSet<Long?>()
        for (withId in this.items) {
            ids.add(withId.id)
        }
        var begginig = 0
        if (this.items.size > 0 && this.items[0].id == BaseRecyclerViewAdapter.Companion.ITEM_VIEW_TYPE_HEADER) {
            begginig = 1
        }
        for (withId in items!!) {
            if (!ids.contains(withId.id)) {
                this.items.add(begginig, withId)
            }
        }
        notifyDataSetChanged()
    }

    fun removeItemAtPosition(position: Int) {
        items!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItemImmediately(item: T?) {
        if (!items!!.contains(item)) return
        items.remove(item)
        notifyDataSetChanged()
    }

//    fun removeItemsImmediately(items: MutableList<T>) {
//        this.items.removeAll(items)
//        notifyDataSetChanged()
//    }

    fun replace(newElement: T) {
        var index = -1
        for (i in getItems()!!.indices) {
            if (getItems()!![i].id === newElement.id) {
                index = i
                break
            }
        }
        if (index > -1) {
            items!![index] = newElement
            notifyItemChanged(index)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun unsubscribe() {
        for (subscription in subscriptions!!) {
            subscription?.unsubscribe()
        }
        subscriptions!!.clear()
    }

    fun subscribe() {}
    fun enableHeaderView() {
        items!!.add(0, object : WithId {
            override val id: Long
                get() = ITEM_VIEW_TYPE_HEADER.toLong()
        } as T)
        notifyItemInserted(0)
    }

    fun disableHeaderView() {
        var hasHeader = false
        for (item in items!!) {
            if (item.id == ITEM_VIEW_TYPE_HEADER) hasHeader =
                true
        }
        if (hasHeader) {
            items.removeAt(0)
            notifyItemInserted(0)
        }
    }

    fun setFooterVisibility(visible: Boolean) {
        if (visible) {
            items!!.add(object : WithId {
                override val id: Long
                    get() = ITEM_VIEW_TYPE_FOOTER.toLong()
            } as T)
            notifyItemInserted(items.size)
        } else {
            var hasFooter = false
            for (item in items!!) {
                if (item.id == ITEM_VIEW_TYPE_FOOTER) hasFooter =
                    true
            }
            if (hasFooter) {
                val size = items.size
                items.removeAt(size - 1)
                notifyItemRemoved(size - 1)
            }
        }
    }

    val isLoadingMore: Boolean
        get() {
            var hasFooter = false
            for (item in items!!) {
                if (item.id == ITEM_VIEW_TYPE_FOOTER) hasFooter =
                    true
            }
            return hasFooter
        }

    override fun getItemViewType(position: Int): Int {
        val id: Long = items!![position].id
        return if (id == ITEM_VIEW_TYPE_FOOTER) {
            ITEM_VIEW_TYPE_FOOTER.toInt()
        } else if (id == ITEM_VIEW_TYPE_HEADER) {
            ITEM_VIEW_TYPE_HEADER.toInt()
        } else {
            ITEM_VIEW_TYPE_DEFAULT.toInt()
        }
    }

    fun notifyElement(item: T?) {
        val indexOf = items!!.indexOf(item)
        if (indexOf > -1) {
            notifyItemChanged(indexOf)
        }
    }

    fun isFooter(pos: Int): Boolean {
        val item: WithId = items!![pos]
        return item.id == ITEM_VIEW_TYPE_FOOTER
    }

    fun isHeader(pos: Int): Boolean {
        val item: WithId = items!![pos]
        return item.id == ITEM_VIEW_TYPE_HEADER
    }

    fun clearData() {
        items!!.clear()
        notifyDataSetChanged()
    }

    companion object {
        const val ITEM_VIEW_TYPE_FOOTER = -1000L
        const val ITEM_VIEW_TYPE_HEADER = -1001L
        const val ITEM_VIEW_TYPE_DEFAULT = 0L
    }

    init {
        setHasStableIds(true)
    }
}