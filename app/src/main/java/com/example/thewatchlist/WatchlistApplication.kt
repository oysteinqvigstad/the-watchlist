package com.example.thewatchlist

import android.app.Application
import com.example.thewatchlist.data.AppContainer
import com.example.thewatchlist.data.DefaultAppContainer

class WatchlistApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}