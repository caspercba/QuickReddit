package com.gaspardeelias.quickreddit.application

import android.app.Application
import com.gaspardeelias.quickreddit.application.QuickRedditApplication.Companion.appComponent

class QuickRedditApplication: Application() {

    companion object {
        var appComponent: AppComponent? = null
    }


    override fun onCreate() {
        super.onCreate()
        buildObjectGraphAndInject()
    }

    private fun buildObjectGraphAndInject() {
        appComponent = DaggerAppComponent.builder()
                .apiModule(ApiModule())
                .build()
    }
}