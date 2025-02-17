package com.example.thewatchlist

import android.app.Application
import com.example.thewatchlist.data.AppContainer
import com.example.thewatchlist.data.DefaultAppContainer
import com.example.thewatchlist.data.persistence.MediaDatabase

/**
 * Application class responsible for initializing and managing AppContainer.
 */
class WatchlistApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()

        MediaDatabase.init(applicationContext)
        container = DefaultAppContainer()
    }
}