package com.gaspardeelias.quickreddit.toplisting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gaspardeelias.quickreddit.R
import com.gaspardeelias.quickreddit.utils.loadCroppedImage
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullScreenImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)
        intent?.let {
            it.data?.let {
                loadCroppedImage(fullScreenImageView, it.toString())
            }
        }
    }

}