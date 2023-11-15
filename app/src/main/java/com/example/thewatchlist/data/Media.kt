package com.example.thewatchlist.data

import com.example.thewatchlist.data.navigation.TopNavOption
import info.movito.themoviedbapi.model.Genre
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvEpisode
import info.movito.themoviedbapi.model.tv.TvSeries
import java.util.Date

/**
 * An interface defining the properties and behaviours common to all media types.
 * This includes movies and TV shows, encapsulating shared attributes like title, genres, and more.
 */
interface Media {
    var status: TopNavOption
    val id: Int
    val title: String
    val overview: String
    val genres: List<Genre>
    val releaseYear: Int
    val posterUrl: String
    val runtime: Pair<Int, Int>
    var notify: Boolean
    var notifyText: String
}

/**
 * Data class representing a Movie.
 * It extends from the Media interface, incorporating specific attributes for movies.
 * This includes the movie's ID, title, runtime, genres, release year, and additional properties.
 */
data class Movie (
    override val id: Int,
    override val title: String,
    override val overview: String,
    override val genres: List<Genre>,
    override val releaseYear: Int,
    override val posterUrl: String,
    override val runtime: Pair<Int, Int>,
    override var status: TopNavOption = TopNavOption.ToWatch,
    override var notify: Boolean = false,
    override var notifyText: String = ""
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

/**
 * Data class representing a TV show.
 * It includes specific attributes for TV shows such as number of episodes, seasons, and last updated date.
 * Extends from the Media interface to include common media properties.
 */
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
    override var notifyText: String = "",
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

/**
 * Data class representing a Season of a TV show.
 * It includes properties like season ID, title, season number, and a list of episodes.
 */
data class Season (
    val id: Int,
    val title: String,
    val seasonNumber: Int,
    var episodes: List<Episode>
)

/**
 * Data class representing an Episode of a TV show.
 * Contains detailed information about a specific episode, including its ID, episode number, season number, air date, title, and overview.
 * Also includes a 'seen' flag to track if the episode has been watched.
 */
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