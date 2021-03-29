package com.gaspardeelias.quickreddit.toplisting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.gaspardeelias.quickreddit.ItemDetailActivity
import com.gaspardeelias.quickreddit.ItemDetailFragment
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.application.QuickRedditApplication
import com.gaspardeelias.quickreddit.utils.getViewModelFromActivity
import com.gaspardeelias.repo.QuickRedditRepo
import com.gaspardeelias.repo.model.TopListingElementDto
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
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
    lateinit var quickRedditRepo: QuickRedditRepo

    private lateinit var adapter : TopListingAdapter
    //val adapter = TopListingAdapter { element, action -> onElementCLick(element, action) }

    private var twoPane: Boolean = false


    private val viewModel: ItemListActivityVM by lazy {
        getViewModelFromActivity {
            ItemListActivityVM(quickRedditRepo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_list)

        QuickRedditApplication.appComponent?.inject(this)

        setSupportActionBar(toolbar)
        toolbar.title = title

        item_detail_container?.let { twoPane = true }

        setupAdapter()
        setupSwipe()
    }

    private fun setupAdapter() {
        adapter = TopListingAdapter(::onElementCLick)
        item_list.adapter = adapter
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                loadStates -> swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.posts.collectLatest {
                adapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { item_list.scrollToPosition(0) }
        }
    }

    private fun setupSwipe() {
        swipe_refresh?.setOnRefreshListener { adapter.refresh() }
    }


    fun onElementCLick(element: TopListingElementDto?, action: Int) {
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

