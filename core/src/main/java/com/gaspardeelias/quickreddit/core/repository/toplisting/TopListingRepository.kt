package com.gaspardeelias.quickreddit.core.repository.toplisting

import androidx.paging.PagingData
import com.gaspardeelias.quickreddit.core.repository.toplisting.model.TopListingElement
import kotlinx.coroutines.flow.Flow


interface TopListingRepository {
    fun listData(pageSize: Int): Flow<PagingData<TopListingElement>>
}