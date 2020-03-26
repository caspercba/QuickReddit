package com.gaspardeelias.quickreddit.core.repository

import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.repository.model.Post
import io.reactivex.subjects.PublishSubject

interface TopListingRepository {

    fun loadTopListing()
    fun listData(): PublishSubject<EndlessList<Post>>
}