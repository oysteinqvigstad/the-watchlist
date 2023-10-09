package com.example.thewatchlist.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.Multi
import info.movito.themoviedbapi.model.tv.TvSeries

@Composable
fun Banner(item: Multi) {
    when (item) {
        is MovieDb -> MovieBanner(movie = item as MovieDb)
        is TvSeries -> TVBanner(tvSeries = item as TvSeries)
    }
}

@Composable
fun TVBanner(tvSeries: TvSeries) {
    Text(text = tvSeries.name)
}

@Composable
fun MovieBanner(movie: MovieDb) {
    Text(text = movie.title)
}
