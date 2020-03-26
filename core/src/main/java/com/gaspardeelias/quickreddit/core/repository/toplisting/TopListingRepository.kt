package com.gaspardeelias.quickreddit.core.repository.toplisting

import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import io.reactivex.Observable


interface TopListingRepository {

    fun loadTopListing()
    fun listData(): Observable<EndlessList<TopListingElement>>
    fun nextPage()
}