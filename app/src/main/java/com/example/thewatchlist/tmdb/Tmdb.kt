package com.example.thewatchlist.tmdb

import android.util.Log
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.TmdbMovies
import info.movito.themoviedbapi.TmdbSearch
import info.movito.themoviedbapi.TmdbTV
import info.movito.themoviedbapi.TmdbTvSeasons
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.core.MovieResultsPage
import info.movito.themoviedbapi.model.tv.TvSeries

object Tmdb {
    private val api : TmdbApi by lazy {
        TmdbApi("c7a5e80f157dd58ea1532f561b5737a9")
    }

    val tmdbMovies : TmdbMovies by lazy {
        api.movies
    }

    val tmdbTvSeries : TmdbTV by lazy {
        api.tvSeries
    }

    val tmdbTvSeasons : TmdbTvSeasons by lazy {
        api.tvSeasons
    }

    val tmdbSearch : TmdbSearch by lazy {
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
}