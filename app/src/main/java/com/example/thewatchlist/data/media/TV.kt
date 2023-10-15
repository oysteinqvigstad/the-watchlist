package com.example.thewatchlist.data.media

import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.network.Tmdb
import com.example.thewatchlist.ui.DataViewModel
import info.movito.themoviedbapi.model.Genre
import info.movito.themoviedbapi.model.tv.TvSeason
import info.movito.themoviedbapi.model.tv.TvSeries
import java.util.Date

class TV(
    id: Int,
    title: String,
    overview: String = "",
    genres: List<Genre> = listOf(),
    private val seasons: List<TvSeason> = listOf(),
    releaseYear: Int
) : Media(
    id = id,
    title = title,
    overview = overview,
    genres = genres,
    releaseYear = releaseYear
), Cloneable {

    constructor(tmdb: TvSeries) : this(
        id = tmdb.id,
        title = tmdb.name,
        overview = tmdb.overview,
        genres = tmdb.genres,
        seasons = tmdb.seasons,
        releaseYear = tmdb.firstAirDate.split("-")[0].toIntOrNull() ?: 0

    )

    val lastUpdate: Date = Date(0)
    var seenList: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        seasons.forEach {
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
            seasons.forEach { season ->
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