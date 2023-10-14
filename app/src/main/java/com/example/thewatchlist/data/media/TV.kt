package com.example.thewatchlist.data.media

import android.util.Log
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.network.Tmdb
import com.example.thewatchlist.ui.DataViewModel
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

    override fun getPrimaryAction(
        activeNavOption: MainNavOption,
        dataViewModel: DataViewModel
    ): Button {
        // TODO: Add logic for returning checkbox or button information
//        if (activeNavOption == MainNavOption.Search) {
        return super.getPrimaryAction(activeNavOption, dataViewModel)
//        } else {
//
//        }
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