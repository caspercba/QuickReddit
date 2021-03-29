package com.gaspardeelias.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gaspardeelias.repo.model.TopListingElementDto
import com.gaspardeelias.repo.net.QuickRedditRetrofit
import kotlinx.coroutines.flow.Flow

class QuickRedditRepoImpl(private val retrofit: QuickRedditRetrofit): QuickRedditRepo {
    override fun posts(pageSize: Int): Flow<PagingData<TopListingElementDto>> = Pager(
        PagingConfig(pageSize)
    ) {
        PagingKeyedSource(retrofit)
    }.flow
}