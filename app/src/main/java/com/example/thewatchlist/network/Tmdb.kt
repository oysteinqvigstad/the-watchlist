package com.example.thewatchlist.network

import android.util.Log
import com.example.thewatchlist.data.media.Media
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.TmdbMovies
import info.movito.themoviedbapi.TmdbSearch
import info.movito.themoviedbapi.TmdbTV
import info.movito.themoviedbapi.TmdbTvSeasons
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.Multi
import info.movito.themoviedbapi.model.core.MovieResultsPage
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface SearchStatus {
    data class Success(val results: List<Media>): SearchStatus
    object Loading: SearchStatus
    object Error: SearchStatus
    object Waiting: SearchStatus
}

object Tmdb {
    private val api : TmdbApi by lazy {
        TmdbApi("c7a5e80f157dd58ea1532f561b5737a9")
    }

    private val tmdbMovies : TmdbMovies by lazy {
        api.movies
    }

    private val tmdbTvSeries : TmdbTV by lazy {
        api.tvSeries
    }

    private val tmdbTvSeasons : TmdbTvSeasons by lazy {
        api.tvSeasons
    }

    private val tmdbSearch : TmdbSearch by lazy {
        api.search
    }

    suspend fun movieInfo(id: Int, language: String) : MovieDb {
        return tmdbMovies.getMovie(id, language)
    }

    suspend fun seriesInfo(id: Int, language: String) : TvSeries {
        return tmdbTvSeries.getSeries(id, language)
    }

    suspend fun searchMovie(text: String,
                    year: Int,
                    language: String,
                    includeAdult: Boolean,
                    page: Int)
    : MovieResultsPage {
        return tmdbSearch.searchMovie(text, year, language, includeAdult, page)
    }

    suspend fun searchSeries() {
        Log.d("Tmdb", "Function not implemented.")
    }

    suspend fun searchAll() {
        Log.d("Tmdb", "Function not implemented.")
    }

    suspend fun searchMulti(title: String): List<Multi>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = tmdbSearch.searchMulti(title, "en", 1).results
                response.mapNotNull { res ->
                    when (res.mediaType) {
                        Multi.MediaType.MOVIE ->
                            tmdbMovies.getMovie((res as MovieDb).id, "en")
                        Multi.MediaType.TV_SERIES ->
                            tmdbTvSeries.getSeries((res as TvSeries).id, "en")
                        else ->
                            null
                    }
                }
            } catch (e: Exception) {
                Log.d("d", e.cause.toString())
                null
            }
        }
    }
}