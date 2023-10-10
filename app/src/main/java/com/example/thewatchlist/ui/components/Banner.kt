package com.example.thewatchlist.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV

@Composable
fun Banner(item: Media) {
    when (item) {
        is Movie -> MovieBanner(movie = item)
        is TV -> TVBanner(tv = item)
    }
}

@Composable
fun TVBanner(tv: TV) {
    Text(text = tv.tmdb.name)
}

@Composable
fun MovieBanner(movie: Movie) {
    Text(text = movie.tmdb.title)
}
