package com.example.thewatchlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV

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
    test()
}


@Preview
@Composable
fun test() {
    LazyColumn {
        item { DetailedTVSeasons(seasonTitle = "Season 1") }
    }
}

@Composable
fun DetailedTVSeasons(seasonTitle: String) {
    var expanded by remember { mutableStateOf(false) }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {

        }
        Text(
            text = seasonTitle,
            style = MaterialTheme.typography.headlineSmall
        )
        if (expanded) {
            DetailedEpisodeList("episode 1", 25)
        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailedEpisodeList(text: String, numberOfEpisodes: Int) {
    FlowRow {

        (1..numberOfEpisodes).forEach {
            CustomCheckboxWithText(episodeNumber = it, isChecked = false)
        }
    }
}


@Composable
fun CustomCheckboxWithText(
    episodeNumber: Int,
    isChecked: Boolean,
//    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {}, // onCheckedChange(!isChecked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Checkbox(
            modifier = Modifier.padding(all = 0.dp),
            checked = isChecked,
            onCheckedChange = {}, //onCheckedChange,
//            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = String.format("%02d", episodeNumber),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}