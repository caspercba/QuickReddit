package com.gaspardeelias.quickreddit.utils


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gaspardeelias.quickreddit.R


fun loadCroppedImage(imageView: ImageView, url: String?) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .error(R.drawable.ic_warning)



    Glide.with(imageView.context).load(url).apply(options)
        .into(imageView)
}