package com.example.thewatchlist.data.media

import android.util.Log
import com.example.thewatchlist.network.Tmdb
import info.movito.themoviedbapi.model.tv.TvSeries
import java.util.Date

class TV(var tmdb: TvSeries) : Media(id = tmdb.id), Cloneable {
    val lastUpdate: Date = Date(0)
    var seenList: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        tmdb.seasons.forEach {
            it.episodes = listOf()
        }
    }


    public override fun clone(): TV {
        return super<Media>.clone() as TV
    }

    suspend fun updateEpisodes() {
        try {
            tmdb.seasons.forEach { season ->
                Tmdb.getEpisodes(this.id, season.seasonNumber)?.let { episodes ->
                    if (episodes.isNotEmpty()) {
                        season.episodes = episodes
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }

    }

}