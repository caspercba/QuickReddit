package com.gaspardeelias.quickreddit.application

import com.gaspardeelias.quickreddit.toplisting.ItemListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class))
interface AppComponent {
    fun inject(itemListActivity: ItemListActivity)
}