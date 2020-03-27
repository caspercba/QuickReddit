package com.gaspardeelias.quickreddit.toplisting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.gaspardeelias.quickreddit.ItemDetailActivity
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.application.QuickRedditApplication
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.domain.getViewModelFromActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
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

    val adapter = TopListingAdapter()

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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        item_list.adapter = adapter
        viewModel.liveData.observe(this, Observer {
            adapter.update(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.attach()
    }

    override fun onPause() {
        super.onPause()
        viewModel.detach()
    }


}

