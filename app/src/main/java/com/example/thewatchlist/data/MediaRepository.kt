package com.example.thewatchlist.data

import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.Multi
import info.movito.themoviedbapi.model.tv.TvEpisode
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MediaRepository {
    suspend fun getMultiMedia(title: String): List<Media>?
}

class NetworkMediaRepository(thunk: () -> TmdbApi) : MediaRepository {

    private val api by lazy { thunk() }



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






    suspend fun getEpisodes(seriesId: Int, seasonId: Int): List<TvEpisode>? {
        return withContext(Dispatchers.IO) {
            try {
                api.tvSeasons.getSeason(seriesId, seasonId, "en").episodes
            } catch (e: Exception) {
                null
            }
        }
    }






}
