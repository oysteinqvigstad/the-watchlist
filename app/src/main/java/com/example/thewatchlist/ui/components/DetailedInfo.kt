package com.example.thewatchlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.ui.DataViewModel
import info.movito.themoviedbapi.model.tv.TvEpisode
import info.movito.themoviedbapi.model.tv.TvSeason

@Composable
fun DetailedInfo(
    media: Media,
    dataViewModel: DataViewModel
) {

    when (media) {
        is Movie -> DetailedMovie(
            movie = media
        )
        is TV -> DetailedTV(
            tv = media,
            onCheckmark = { state, tv -> dataViewModel.setEpisodeCheckmark(state, tv, media) }
        )
    }
}

@Composable
fun DetailedMovie(
    movie: Movie
) {
    Text(text = movie.title)
    Text(text = movie.releaseYear.toString())
    Text(text = formatMovieLength(movie.runtime))
    Text(text = movie.overview)
}

@Composable
fun DetailedTV(
    tv: TV,
    onCheckmark: (Boolean, TvEpisode) -> Unit
) {

    LaunchedEffect(Unit) {
        // This updates way too often!!

    }

    LazyColumn {
        item { Text(text = tv.title) }
//        item { Text(text = tv.) }
//        item { Text(text = tv.tmdb) }
        item { Text(text = tv.overview) }
        tv.seasons.forEach {
            item { DetailedTVSeasons(it, tv.seenList, onCheckmark) }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedTVSeasons(
    season: TvSeason,
    seenList: MutableSet<Pair<Int, Int>>,
    onCheckmark: (Boolean, TvEpisode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val seenSeasonCount = seenList.count { it.first == season.seasonNumber }
    val seenAll = seenSeasonCount == season.episodes.size
    val tristate = {
        if (seenAll) ToggleableState.On
        else if (seenSeasonCount == 0) ToggleableState.Off
        else ToggleableState.Indeterminate
    }

    ListItem(
        modifier = Modifier.clickable { expanded = !expanded },
        headlineContent = {
            Text(
                text = season.name,
//                modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
            )
        },
        supportingContent = {
            Column {
                Text(text = String.format("%d episodes remaining", season.episodes.size - seenSeasonCount))
                LinearProgressIndicator(
                    progress = seenSeasonCount.toFloat() / season.episodes.size.toFloat(),
                    modifier = Modifier.padding(all = 0.dp)
                )

            }
        },
        leadingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TriStateCheckbox(
                    state = tristate(),
                    onClick = {}
                )
//                Badge(
//                    contentColor = if (seenAll) Color.Black else Color.White,
//                    containerColor = if (seenAll) Color.Green else Color.Red
//                ) {
//                    Text(text = String.format("%d/%d", seenSeasonCount, season.episodes.size))
//                }

            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Collapse/Expand arrow",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(if (expanded) 0f else 180f)
            )

        },
        tonalElevation = 2.dp,
    )
    HorizontalDivider()

        if (expanded) {
            DetailedEpisodeList(season, seenList, onCheckmark)
        }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailedEpisodeList(
    season: TvSeason,
    seenList: MutableSet<Pair<Int, Int>>,
    onCheckmark: (Boolean, TvEpisode) -> Unit) {


    season.episodes.forEach {
        ListItem(
            headlineContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = String.format("S%02dE%02d: %s", it.seasonNumber, it.episodeNumber, it.name))
                } },
//            supportingContent = {
//                Text(text = it.overview, maxLines = 3, overflow = TextOverflow.Ellipsis)
//            },
            leadingContent = {
                Checkbox(
                checked = seenList.contains(Pair(it.seasonNumber, it.episodeNumber)),
                onCheckedChange = {state -> onCheckmark(state, it)})
            }
        )
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
        )
        Text(
            text = String.format("%02d", episodeNumber),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}