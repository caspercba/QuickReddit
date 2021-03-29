package com.gaspardeelias.repo.net

import com.gaspardeelias.repo.model.PostListing
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface QuickRedditRetrofit {

    @GET("/top")
    suspend fun getTopListing(
        @Query("after") after: String? = null,
        @Query ("before") before: String? = null,
        @Query ("limit") limit: Int) : PostListing

    companion object {
        fun create(retrofit: Retrofit): QuickRedditRetrofit =
            retrofit.create(QuickRedditRetrofit::class.java)
    }
}