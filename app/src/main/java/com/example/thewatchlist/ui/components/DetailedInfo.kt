package com.example.thewatchlist.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.MainNavOption

@Composable
fun DetailedInfo(media: Media) {

    when (media) {
        is Movie -> DetailedMovie(
            movie = media,
        )
        is TV -> DetailedTV(
            tv = media,
        )
    }
}

@Composable
fun DetailedMovie(movie: Movie) {
    Text(text = movie.tmdb.title)
    Text(text = movie.tmdb.releaseDate)
    Text(text = movie.tmdb.runtime.toString())
    Text(text = movie.tmdb.overview)
}

@Composable
fun DetailedTV(tv: TV) {
    Text(text = tv.tmdb.name)
    Text(text = tv.tmdb.firstAirDate)
    Text(text = tv.tmdb.episodeRuntime.toString())
    Text(text = tv.tmdb.overview)
}
