package com.example.thewatchlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.MainNavOption

@Composable
fun Banner(
    item: Media,
    activeBottomNav: MainNavOption,
    onClick: (Media) -> Unit
) {

    when (item) {
        is Movie -> MovieBanner(
            movie = item,
            activeBottomNav = MainNavOption.Search,
            onClick = onClick
        )
        is TV -> TVBanner(
            tv = item,
            activeBottomNav = MainNavOption.Search,
            onClick = onClick
        )
    }
}

@Composable
fun TVBanner(
    tv: TV,
    activeBottomNav: MainNavOption,
    onClick: (Media) -> Unit
) {
    Text(text = "TV: " + tv.tmdb.name, modifier = Modifier.clickable { onClick(tv) })
}

@Composable
fun MovieBanner(
    movie: Movie,
    activeBottomNav: MainNavOption,
    onClick: (Media) -> Unit
) {
    Text(text = "Movie: " + movie.tmdb.title, modifier = Modifier.clickable { onClick(movie) })
}
