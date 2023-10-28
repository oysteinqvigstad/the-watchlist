package com.example.thewatchlist.network

import android.util.Log
import com.example.thewatchlist.data.Media
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.TmdbMovies
import info.movito.themoviedbapi.TmdbSearch
import info.movito.themoviedbapi.TmdbTV
import info.movito.themoviedbapi.TmdbTvSeasons
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.Multi
import info.movito.themoviedbapi.model.core.MovieResultsPage
import info.movito.themoviedbapi.model.tv.TvEpisode
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Sealed interface representing the different states of a search operation.
 */
sealed interface SearchStatus {
    object Success: SearchStatus
    object Loading: SearchStatus
    object Error: SearchStatus
    object NoAction: SearchStatus
}

/**
 * Singleton object representing the TMDB API and its various endpoints.
 */
object Tmdb {
    // Lazily initialize the TMDB API and its endpoints using API key.
    private val api : TmdbApi by lazy {
        TmdbApi("c7a5e80f157dd58ea1532f561b5737a9")
    }

    // Lazily initialize TMDB movie endpoint.
    private val tmdbMovies : TmdbMovies by lazy {
        api.movies
    }

    // Lazily initialize TMDB TV series endpoint.
    private val tmdbTvSeries : TmdbTV by lazy {
        api.tvSeries
    }

    // Lazily initialize TMDB TV seasons endpoint.
    private val tmdbTvSeasons : TmdbTvSeasons by lazy {
        api.tvSeasons
    }

    // Lazily initialize TMDB search endpoint.
    private val tmdbSearch : TmdbSearch by lazy {
        api.search
    }

    /**
     * Get detailed information about a movie by its ID and language.
     *
     * @param id The ID of the movie.
     * @param language The language for the information.
     * @return Detailed movie information.
     */
    suspend fun movieInfo(id: Int, language: String) : MovieDb {
        return tmdbMovies.getMovie(id, language)
    }

    /**
     * Get detailed information about a TV series by its ID and language.
     *
     * @param id The ID of the TV series.
     * @param language The language for the information.
     * @return Detailed TV series information.
     */
    suspend fun seriesInfo(id: Int, language: String) : TvSeries {
        return tmdbTvSeries.getSeries(id, language)
    }

    /**
     * Search for movies based on the provided criteria.
     *
     * @param text The search text.
     * @param year The release year filter.
     * @param language The language for the search.
     * @param includeAdult Include adult content in the search results.
     * @param page The page number of search results.
     * @return Results page containing movie search results.
     */
    suspend fun searchMovie(text: String,
                    year: Int,
                    language: String,
                    includeAdult: Boolean,
                    page: Int)
    : MovieResultsPage {
        return tmdbSearch.searchMovie(text, year, language, includeAdult, page)
    }

    /**
     * Search for media items (movies and TV series) using a common title.
     *
     * @param title The title to search for.
     * @return List of Multi items that match the search.
     */
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

    /**
     * Get the list of episodes for a TV series and season by their IDs.
     *
     * @param seriesId The ID of the TV series.
     * @param seasonId The ID of the season.
     * @return List of TV episodes for the specified series and season.
     */
    suspend fun getEpisodes(seriesId: Int, seasonId: Int): List<TvEpisode>? {
        return withContext(Dispatchers.IO) {
            try {
                tmdbTvSeasons.getSeason(seriesId, seasonId, "en").episodes
            } catch (e: Exception) {
                null
            }
        }
    }

}