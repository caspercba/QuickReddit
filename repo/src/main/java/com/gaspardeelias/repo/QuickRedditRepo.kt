package com.gaspardeelias.repo

import androidx.paging.PagingData
import com.gaspardeelias.repo.model.TopListingElementDto
import kotlinx.coroutines.flow.Flow

interface QuickRedditRepo {
    fun posts(pageSize: Int): Flow<PagingData<TopListingElementDto>>
}