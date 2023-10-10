package com.example.thewatchlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.MainNavOption

@Composable
fun Banner(
    media: Media,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
    onAdd: (Media) -> Unit
) {

    when (media) {
        is Movie -> MovieBanner(
            movie = media,
            activeBottomNav = MainNavOption.Search,
            onAdd = onAdd,
            onDetails = onDetails
        )
        is TV -> TVBanner(
            tv = media,
            activeBottomNav = MainNavOption.Search,
            onAdd = onAdd,
            onDetails = onDetails
        )
    }
}

@Composable
fun TVBanner(
    tv: TV,
    activeBottomNav: MainNavOption,
    onAdd: (Media) -> Unit,
    onDetails: (Media) -> Unit
) {
    Row {
        Text(text = "TV: " + tv.tmdb.name, modifier = Modifier.clickable { onDetails(tv) })
        Spacer(modifier = Modifier.padding(start = 2.dp))
        Text(text = "+", modifier = Modifier.clickable { onAdd(tv) })
    }
}

@Composable
fun MovieBanner(
    movie: Movie,
    activeBottomNav: MainNavOption,
    onAdd: (Media) -> Unit,
    onDetails: (Media) -> Unit
) {
    Row {
        Text(text = "Movie: " + movie.tmdb.title, modifier = Modifier.clickable { onDetails(movie) })
        Spacer(modifier = Modifier.padding(start = 2.dp))
        Text(text = "+", modifier = Modifier.clickable { onAdd(movie) })
    }
}
