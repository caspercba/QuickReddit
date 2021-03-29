package com.gaspardeelias.quickreddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.gaspardeelias.quickreddit.toplisting.FullScreenImageActivity
import com.gaspardeelias.quickreddit.toplisting.ItemListActivity
import com.gaspardeelias.quickreddit.utils.loadCroppedImage
import com.gaspardeelias.repo.model.TopListingElementDto
import kotlinx.android.synthetic.main.activity_item_detail.*
import org.jetbrains.anko.onClick

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {

    var element: TopListingElementDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = ItemDetailFragment().apply {

                element = intent.getParcelableExtra<TopListingElementDto>(ItemDetailFragment.ARG_ITEM)
                arguments = Bundle().apply {
                    putParcelable(ItemDetailFragment.ARG_ITEM,
                            element)
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        element?.let {
            loadCroppedImage(id_header_image, it.thumbnail)
            id_header_image.onClick {
                startActivity(Intent(this, FullScreenImageActivity::class.java).apply { setData(Uri.parse(element!!.thumbnail)) })
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    navigateUpTo(Intent(this, ItemListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}