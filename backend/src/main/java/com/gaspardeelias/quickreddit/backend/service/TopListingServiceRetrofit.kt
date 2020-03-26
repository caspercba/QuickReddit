package com.gaspardeelias.quickreddit.backend.service


import com.gaspardeelias.quickreddit.core.service.PostListingDto
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET

interface TopListingServiceRetrofit {

    @GET("/api/top")
    fun getTopListing() : Observable<PostListingDto>

    companion object {
        fun create(retrofit: Retrofit): TopListingServiceRetrofit =
            retrofit.create(TopListingServiceRetrofit::class.java)
    }
}