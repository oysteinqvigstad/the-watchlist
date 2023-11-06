package com.example.thewatchlist.data

import com.example.thewatchlist.data.navigation.TopNavOption
import info.movito.themoviedbapi.model.Genre
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvEpisode
import info.movito.themoviedbapi.model.tv.TvSeries
import java.util.Date


interface Media { // make this data class

    var status: TopNavOption
    val id: Int
    val title: String
    val overview: String
    val genres: List<Genre>
    val releaseYear: Int
    val posterUrl: String
    val runtime: Pair<Int, Int>
    var notify: Boolean
}

data class Movie (
    override val id: Int,
    override val title: String,
    override val overview: String,
    override val genres: List<Genre>,
    override val releaseYear: Int,
    override val posterUrl: String,
    override val runtime: Pair<Int, Int>,
    override var status: TopNavOption = TopNavOption.ToWatch,
    override var notify: Boolean = false
) : Media {
    constructor(tmdb: MovieDb) : this(
        id = tmdb.id,
        title = tmdb.title,
        overview = tmdb.overview,
        genres = tmdb.genres,
        releaseYear = tmdb.releaseDate.split("-")[0].toIntOrNull() ?: 0,
        posterUrl =  "https://image.tmdb.org/t/p/w200" + tmdb.posterPath,
        runtime = Pair(tmdb.runtime / 60, tmdb.runtime % 60),
    )
}

data class TV (
    override val id: Int,
    override val title: String,
    override val overview: String,
    override val genres: List<Genre>,
    override val releaseYear: Int,
    override val runtime: Pair<Int, Int>,
    override var status: TopNavOption = TopNavOption.ToWatch,
    override val posterUrl: String,
    override var notify: Boolean = false,
    var numberOfEpisodes: Int,
    var seasons: List<Season> = listOf(),
    var lastUpdated: Date = Date(0)
) : Media {
    constructor(tmdb: TvSeries) : this(
        id = tmdb.id,
        title = tmdb.name,
        overview = tmdb.overview,
        genres = tmdb.genres,
        releaseYear = tmdb.firstAirDate.split("-")[0].toIntOrNull() ?: 0,
        posterUrl = "https://image.tmdb.org/t/p/w200" + tmdb.posterPath,
        numberOfEpisodes = tmdb.numberOfEpisodes,
        runtime = Pair(tmdb.episodeRuntime.getOrElse(0) { 0 } / 60, tmdb.episodeRuntime.getOrElse(0) { 0 } % 60),
        seasons = tmdb.seasons.map { Season(
            id = it.id,
            seasonNumber = it.seasonNumber,
            episodes = listOf(),
            title = it.name
        ) }
    )
}


data class Season (
    val id: Int,
    val title: String,
    val seasonNumber: Int,
    var episodes: List<Episode>
)

data class Episode (
    val id: Int,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val airDate: String,
    val title: String,
    val overview: String,
    var seen: Boolean
) {
    constructor(tmdb: TvEpisode) : this(
        id = tmdb.id,
        episodeNumber = tmdb.episodeNumber,
        seasonNumber = tmdb.seasonNumber,
        airDate = tmdb.airDate ?: "",
        title = tmdb.name,
        overview = tmdb.overview,
        seen = false
    )
}