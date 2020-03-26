package com.gaspardeelias.quickreddit.core.repository.toplisting

import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManager
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManagerImpl
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService
import io.reactivex.Observable

class TopListingRepositoryImpl(val service: TopListingService) :
    TopListingRepository {

    private val topListingManager: EndlessListManager<TopListingElement> = EndlessListManagerImpl()

    override fun loadTopListing() {

    }

    override fun listData(): Observable<EndlessList<TopListingElement>> = topListingManager.list

}