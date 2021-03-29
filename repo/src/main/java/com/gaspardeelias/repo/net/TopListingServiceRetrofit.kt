package com.gaspardeelias.repo.net

import com.gaspardeelias.repo.model.TopListingDto
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface TopListingServiceRetrofit {

    @GET("/top")
    suspend fun getTopListing(
        @Query("after") after: String? = null,
        @Query ("before") before: String? = null,
        @Query ("limit") limit: Int) : TopListingDto

    companion object {
        fun create(retrofit: Retrofit): TopListingServiceRetrofit =
            retrofit.create(TopListingServiceRetrofit::class.java)
    }
}