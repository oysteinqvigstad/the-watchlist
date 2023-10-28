package com.example.thewatchlist.data

import info.movito.themoviedbapi.TmdbApi

/**
 * Interface representing a container for application dependencies.
 */
interface AppContainer {
    val mediaRepository: MediaRepository
}

/**
 * Default implementation of the [AppContainer] interface, providing dependencies such as the [MediaRepository].
 */
class DefaultAppContainer : AppContainer {
    // Lazily initialize the TmdbApi instance with the API key.
    private val api: TmdbApi by lazy {
        TmdbApi("c7a5e80f157dd58ea1532f561b5737a9")
    }

    // Lazily initialize the media repository with the TmdbApi instance.
    override val mediaRepository: MediaRepository by lazy {
        NetworkMediaRepository { api }
    }

}
