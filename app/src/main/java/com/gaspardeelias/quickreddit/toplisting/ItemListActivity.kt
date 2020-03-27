package com.gaspardeelias.quickreddit.toplisting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaspardeelias.quickreddit.ItemDetailActivity
import com.gaspardeelias.quickreddit.ItemDetailFragment
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.application.QuickRedditApplication
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.domain.getViewModelFromActivity
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import org.jetbrains.anko.onClick
import javax.inject.Inject

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    @Inject
    lateinit var topListingRepository: TopListingRepository

    val adapter = TopListingAdapter { onElementCLick(it) }

    private var twoPane: Boolean = false
    private val viewModel: ItemListActivityVM by lazy {
        getViewModelFromActivity {
            ItemListActivityVM(
                topListingRepository
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        QuickRedditApplication.appComponent?.inject(this)

        setSupportActionBar(toolbar)
        toolbar.title = title

        item_detail_container?.let { twoPane = true }
        item_list?.adapter = adapter
        var manager = item_list?.layoutManager as LinearLayoutManager

        item_list?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(swipe_refresh.isRefreshing) {
                    return
                }
                val visibleItemCount = manager.childCount
                val totalItemCount = manager.itemCount
                val pastVisiblesItems = manager.findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisiblesItems >= totalItemCount && totalItemCount > 1) {
                    viewModel.nextPage()
                }
            }
        })
        viewModel.liveData.observe(this, Observer {
            adapter.update(it)
            swipe_refresh?.isRefreshing = false
        })

        id_dismiss?.onClick { adapter.removeAll() }
        swipe_refresh?.setOnRefreshListener {
            swipe_refresh?.isRefreshing = true
            viewModel.refresh()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.attach()
    }

    override fun onPause() {
        super.onPause()
        viewModel.detach()
    }

    fun onElementCLick(element: TopListingElement?) {
        element?.viewed = true
        element?.let { adapter.updateItem(it) }
        if (twoPane) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ItemDetailFragment.ARG_ITEM, element)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM, element)
            }
            startActivity(intent)
        }
    }


}

