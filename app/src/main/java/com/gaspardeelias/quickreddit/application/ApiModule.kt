package com.gaspardeelias.quickreddit.application

import com.gaspardeelias.quickreddit.backend.service.TopListingServiceImpl
import com.gaspardeelias.quickreddit.backend.service.TopListingServiceRetrofit
import com.gaspardeelias.quickreddit.core.service.TopListingService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun providesTopListingService(retrofit: Retrofit): TopListingService {
        return TopListingServiceImpl(TopListingServiceRetrofit.create(retrofit))
    }
}