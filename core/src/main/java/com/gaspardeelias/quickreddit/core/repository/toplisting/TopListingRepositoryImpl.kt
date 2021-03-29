package com.gaspardeelias.quickreddit.core.repository.toplisting

import androidx.paging.Pager
import androidx.paging.PagingData
import com.gaspardeelias.quickreddit.core.kernel.*
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessList
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManager
import com.gaspardeelias.quickreddit.core.kernel.list.EndlessListManagerImpl
import com.gaspardeelias.quickreddit.core.kernel.model.BasicError
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.converters.TopListingConverter
import com.gaspardeelias.quickreddit.core.repository.toplisting.converters.TopListingConverter.Companion.convertTopListingElement
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingResponse
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService
import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Flow

class TopListingRepositoryImpl(val service: TopListingService) :
    TopListingRepository {

    override fun listData(pageSize: Int): Flow<PagingData<TopListingElement>> = Pager(
        PageKe
    ).flow

    //private val topListingManager: EndlessListManager<TopListingElement> = EndlessListManagerImpl()

    //override fun loadTopListing() {
    //    buildTopListingResponse(service.getTopListing(), false)
    //}

    //override fun listData(): Observable<EndlessList<TopListingElement>> = topListingManager.list

}