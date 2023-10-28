package com.example.thewatchlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewatchlist.data.Episode
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.Season
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.ui.DataViewModel
import androidx.compose.ui.text.style.TextAlign
import com.example.thewatchlist.ui.theme.Purple40


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
            onCheckmark = { state, tv, episode -> dataViewModel.setEpisodeCheckmark(state, tv, episode) },
            onSeasonCheckmark = { state, tv, season -> dataViewModel.setSeasonCheckmark(state, tv, season) }
        )
    }
}

@Composable
fun DetailedTopInfo(description: String, map: Map<String, String>) {

    Text(
        text = "Description",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    HorizontalDivider(thickness = 2.dp, color = Purple40)
    Spacer(modifier = Modifier.height(4.dp))

    Text(text = description, textAlign = TextAlign.Justify)

    Spacer(modifier = Modifier.height(12.dp))

    map.forEach() { data ->

        if (data.value.isNotEmpty()) {

            Row(
                //modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = data.key,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.width(120.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                VerticalDivider(
                    thickness = 2.dp,
                    color = Purple40,
                    modifier = Modifier.height(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))
                Text(text = data.value, fontSize = 16.sp, modifier = Modifier.width(120.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun DetailedMovie(
    movie: Movie
) {
    LazyColumn(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { DetailedTopInfo(
            movie.overview,
            mapOf(
                "Release Year" to movie.releaseYear.toString(),
                "Movie length" to formatMovieLength(movie.runtime).toString()
            )
        ) }
    }

}

@Composable
fun DetailedTV(
    tv: TV,
    onCheckmark: (Boolean, TV, Episode) -> Unit,
    onSeasonCheckmark: (Boolean, TV, Season) -> Unit
) {

    LazyColumn(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item { DetailedTopInfo(
            tv.overview,
            mapOf(
                "Release Year" to tv.releaseYear.toString(),
                "Seasons" to tv.seasons.size.toString()
            )
        ) }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        tv.seasons.forEach {
            item { DetailedTVSeasons(
                tv = tv,
                season = it,
                onCheckmark = onCheckmark,
                onSeasonCheckmark = onSeasonCheckmark
            )}
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedTVSeasons(
    tv: TV,
    season: Season,
    onCheckmark: (Boolean, TV, Episode) -> Unit,
    onSeasonCheckmark: (Boolean, TV, Season) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val seenSeasonCount = season.episodes.count { it.seen }
    val seenAll = seenSeasonCount == season.episodes.size
    val tristate = {
        if (seenAll) ToggleableState.On
        else if (seenSeasonCount == 0) ToggleableState.Off
        else ToggleableState.Indeterminate
    }

    ElevatedCard(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {

        ListItem(
            modifier = Modifier.clickable { expanded = !expanded },
            headlineContent = {
                Text(
                    text = season.title,
                )
            },
            supportingContent = {
                Column {
                    Text(
                        text = String.format(
                            "%d episodes remaining",
                            season.episodes.size - seenSeasonCount
                        )
                    )
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
                        onClick = { onSeasonCheckmark(!seenAll, tv, season) }
                    )
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
        )
    }

    if (expanded) {
        DetailedEpisodeList(tv, season, onCheckmark)
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailedEpisodeList(
    tv: TV,
    season: Season,
    onCheckmark: (Boolean, TV, Episode) -> Unit
) {


    Column(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {

        season.episodes.forEach {
            ListItem(
                headlineContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = String.format("S%02dE%02d: %s", it.seasonNumber, it.episodeNumber, it.title))
                    } },
                leadingContent = {
                    Checkbox(
                        checked = it.seen,
                        onCheckedChange = { state -> onCheckmark(state, tv, it) })
                }
            )
        }

    }

}


@Composable
fun CustomCheckboxWithText(
    episodeNumber: Int,
    isChecked: Boolean,
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