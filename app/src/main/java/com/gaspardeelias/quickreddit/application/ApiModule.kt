package com.gaspardeelias.quickreddit.application

import android.app.Application
import com.gaspardeelias.repo.QuickRedditRepo
import com.gaspardeelias.repo.QuickRedditRepoImpl
import com.gaspardeelias.repo.net.QuickRedditRetrofit
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
    fun providesTopListingRepository(retrofit: Retrofit) : QuickRedditRetrofit {
        return QuickRedditRetrofit.create(retrofit)
    }

    @Provides
    @Singleton
    fun provideQuickRedditRepo(quickRedditRetrofit: QuickRedditRetrofit)
    : QuickRedditRepo {
        return QuickRedditRepoImpl(quickRedditRetrofit)
    }

    @Provides
    @Singleton
    fun providesRetrofit2(
        gson: Gson
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
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()
    }

}