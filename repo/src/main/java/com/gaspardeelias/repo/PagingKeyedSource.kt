package com.gaspardeelias.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gaspardeelias.repo.model.TopListingElementDto
import com.gaspardeelias.repo.net.QuickRedditRetrofit
import retrofit2.HttpException
import java.io.IOException

class PagingKeyedSource(val api: QuickRedditRetrofit): PagingSource<String, TopListingElementDto>() {
    override fun getRefreshKey(state: PagingState<String, TopListingElementDto>): String? {
        return state.anchorPosition?.let { anchorPosition -> state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, TopListingElementDto> {
        return try {
            val data = api.getTopListing(
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                limit = params.loadSize
            ).data

            LoadResult.Page(
                data = data.children.map { it.data },
                prevKey = data.before,
                nextKey = data.after
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}