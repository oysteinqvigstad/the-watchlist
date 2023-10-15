package com.example.thewatchlist.data.media

import androidx.compose.ui.graphics.Color
import com.example.thewatchlist.data.navigation.TopNavOption
import info.movito.themoviedbapi.model.Genre
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvSeries

data class Button(
    val label: String,
    val action: (Media) -> Unit,
    val color: Color
)

interface Media {

    var status: TopNavOption
    val id: Int
    val title: String
    val overview: String
    val genres: List<Genre>
    val releaseYear: Int
    val runtime: Pair<Int, Int>
}

data class Movie (
    override val id: Int,
    override val title: String,
    override val overview: String,
    override val genres: List<Genre>,
    override val releaseYear: Int,
    override val runtime: Pair<Int, Int>,
    override var status: TopNavOption = TopNavOption.ToWatch
) : Media {
    constructor(tmdb: MovieDb) : this(
        id = tmdb.id,
        title = tmdb.title,
        overview = tmdb.overview,
        genres = tmdb.genres,
        releaseYear = tmdb.releaseDate.split("-")[0].toIntOrNull() ?: 0,
        runtime = Pair(tmdb.runtime / 60, tmdb.runtime % 60)
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
    var seenList: MutableSet<Pair<Int, Int>> = mutableSetOf()
) : Media {
    constructor(tmdb: TvSeries) : this(
        id = tmdb.id,
        title = tmdb.name,
        overview = tmdb.overview,
        genres = tmdb.genres,
        releaseYear = tmdb.firstAirDate.split("-")[0].toIntOrNull() ?: 0,
        runtime = Pair(tmdb.episodeRuntime.getOrElse(0) { 0 } / 60, tmdb.episodeRuntime.getOrElse(0) { 0 } % 60)

    )
}