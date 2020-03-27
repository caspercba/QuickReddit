package com.gaspardeelias.quickreddit.application

import android.app.Application
import com.gaspardeelias.quickreddit.backend.service.TopListingServiceImpl
import com.gaspardeelias.quickreddit.backend.service.TopListingServiceRetrofit
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepository
import com.gaspardeelias.quickreddit.core.repository.toplisting.TopListingRepositoryImpl
import com.gaspardeelias.quickreddit.core.service.toplisting.TopListingService
import com.google.gson.*
import com.google.gson.internal.bind.DateTypeAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class ApiModule(val app: QuickRedditApplication) {

    @Provides
    @Singleton
    fun providesTopListingService(retrofit: Retrofit): TopListingService {
        return TopListingServiceImpl(TopListingServiceRetrofit.create(retrofit))
    }

    @Provides
    @Singleton
    fun providesTopListingRepository(topListingService: TopListingService) : TopListingRepository {
        return TopListingRepositoryImpl(topListingService)
    }

    @Provides
    @Singleton
    fun providesRetrofit2(
        app: Application,
        gson: Gson?
    ): Retrofit {
        val httpClient = OkHttpClient.Builder()
        //httpClient.addInterceptor(new LogJsonInterceptor());
        return Retrofit.Builder()
            .baseUrl("https://api.reddit.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideGson(): Gson? {
        return GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()
    }
}