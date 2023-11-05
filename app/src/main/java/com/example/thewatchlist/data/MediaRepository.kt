package com.example.thewatchlist.data

import android.util.Log
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.Multi
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Interface representing a repository for media-related data.
 */
interface MediaRepository {
    suspend fun getMultiMedia(title: String): List<Media>?
    suspend fun getEpisodes(seriesId: Int, seasonId: Int): List<Episode>?
    suspend fun getSeries(seriesId: Int): TV?
}

/**
 * Sealed interface representing the different states of a search operation.
 */
sealed interface SearchStatus {
    object Success: SearchStatus
    object Loading: SearchStatus
    class Error(val message: String): SearchStatus
    object NoAction: SearchStatus
}

/**
 * Implementation of the MediaRepository that retrieves data from a network source using the TmdbApi.
 *
 * @param thunk A function that provides an instance of TmdbApi.
 */
class NetworkMediaRepository(thunk: () -> TmdbApi) : MediaRepository {
    private val api by lazy { thunk() }

    /**
     * Get a list of media items (movies and TV series) based on a common title.
     *
     * @param title The title to search for.
     * @return List of Media items that match the search.
     */
    override suspend fun getMultiMedia(title: String): List<Media>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.search.searchMulti(title, "en", 1).results
                response.mapNotNull { res ->
                    when (res.mediaType) {
                        Multi.MediaType.MOVIE ->
                            Movie(api.movies.getMovie((res as MovieDb).id, "en"))
                        Multi.MediaType.TV_SERIES ->
                            TV(api.tvSeries.getSeries((res as TvSeries).id, "en"))
                        else ->
                            null
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    /**
     * Get detailed information about a TV series by its ID.
     *
     * @param seriesId The ID of the TV series.
     * @return Detailed TV series information.
     */
    override suspend fun getSeries(seriesId: Int): TV? {
        return withContext(Dispatchers.IO) {
            try {
                TV(api.tvSeries.getSeries(seriesId, "en"))
            } catch (e: Exception) {
                null
            }
        }
    }


    /**
     * Get a list of episodes for a TV series and season by their IDs.
     *
     * @param seriesId The ID of the TV series.
     * @param seasonId The ID of the season.
     * @return List of Episode items for the specified series and season.
     */
    override suspend fun getEpisodes(seriesId: Int, seasonId: Int): List<Episode>? {
        return withContext(Dispatchers.IO) {
            try {
                api.tvSeasons.getSeason(seriesId, seasonId, "en").episodes.map { Episode(it) }
            } catch (e: Exception) {
                null
            }
        }
    }

}
