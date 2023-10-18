package com.example.thewatchlist.data

import info.movito.themoviedbapi.TmdbApi

interface AppContainer {
    val mediaRepository: MediaRepository
    val photoRepository: PhotoRepository
}

class DefaultAppContainer : AppContainer {
    private val api: TmdbApi by lazy {
        TmdbApi("c7a5e80f157dd58ea1532f561b5737a9")
    }

    override val mediaRepository: MediaRepository by lazy {
        NetworkMediaRepository { api }
    }
    override val photoRepository = NetworkPhotoRepository()

}
