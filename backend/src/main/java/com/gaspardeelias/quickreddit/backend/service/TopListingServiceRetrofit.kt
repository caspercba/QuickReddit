package com.gaspardeelias.quickreddit.backend.service


import com.gaspardeelias.quickreddit.core.service.toplisting.dto.TopListingDto
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface TopListingServiceRetrofit {

    @GET("/top")
    fun getTopListing(@Query ("after") after: String? = null) : Observable<TopListingDto>

    companion object {
        fun create(retrofit: Retrofit): TopListingServiceRetrofit =
            retrofit.create(TopListingServiceRetrofit::class.java)
    }
}