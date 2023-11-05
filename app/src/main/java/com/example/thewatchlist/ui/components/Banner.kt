package com.example.thewatchlist.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.thewatchlist.data.Episode
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.screens.LoadingIndicator
import com.example.thewatchlist.ui.theme.KashmirBlue
import com.example.thewatchlist.ui.theme.RemoveColor
import info.movito.themoviedbapi.model.Genre

/**
 * Composable function to display a banner for a media item with details.
 *
 * @param media The media item to display in the banner.
 * @param dataViewModel The view model containing data and actions related to media items.
 * @param activeBottomNav The active bottom navigation option.
 * @param onDetails Callback to handle details click.
 */
@Composable
fun Banner(
    media: Media,
    dataViewModel: DataViewModel,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
) {

    LaunchedEffect(Unit) {
        if (media is TV) {
            dataViewModel.updateTVShow(media)
        }
    }

    // Asynchronously load the image for the media item
    val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
        .data(media.posterUrl)
        .size(coil.size.Size.ORIGINAL)
        .build()
    )

    // Create an elevated card for the banner
    ElevatedCard(
        modifier = Modifier
            .padding(6.dp)
            .height(160.dp)
            .clickable { onDetails(media) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Box (modifier = Modifier
                .width(110.dp)
                .fillMaxHeight()){
                // Display loading text or the media item image
                if (painter.state is AsyncImagePainter.State.Loading) {
                    LoadingIndicator()
                } else {
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(110.dp)
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.medium),
                        contentDescription = "Film poster"
                    )
                }
            }
            // Display media details in a column
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                TitleHeader(title = media.title)

                if (media is TV && activeBottomNav != MainNavOption.Search) {
                    val nextEpisode = dataViewModel.getNextUnwatchedEpisode(media)
                    ProgressBarRemainingEpisodes(media)
                } else {
                    BriefInfoText(media = media)
                    GenresText(genres = media.genres)
                }

                // Display primary action button based on the active bottom navigation and media status
                BannerPrimaryAction(media = media, activeBottomNav = activeBottomNav, dataViewModel = dataViewModel)
            }

        }

    }
}

/**
 *  Composable function for displaying the title header
 */
@Composable
fun TitleHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 5.dp)
    )
}

@Composable
fun GenresText(genres: List<Genre>) {
    Text(text = genres.take(3).joinToString(", ") { it.name },
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun BriefInfoText(media: Media) {
    Text(
        text = media.releaseYear.let { if (it == 0) "TBA" else it.toString() } +
                (formatMovieLength(media.runtime)?.let { " \u2022 $it" } ?: "") +
                if (media is TV) " \u2022 " + media.numberOfEpisodes + " episodes" else ""
        ,
        fontSize = 12.sp,
    )
}

@Composable
fun NextEpisodeInfo(episode: Episode?, onCheckbox: () -> Unit) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (episode != null) {
            Column {
                Text(
                    text = "Next episode",
                    fontSize = 12.sp
                )

                Text(
                    text = String.format(
                        "S%02dE%02d",
                        episode.seasonNumber,
                        episode.episodeNumber,
                    ),
                )
            }
            Button(
                onClick = { onCheckbox() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KashmirBlue
                ),
                modifier = Modifier.padding(top = 13.dp).height(35.dp)
            ) {
                Text(text = "Seen it!", fontSize = 13.sp)
            }

        }
    }
}



@Composable
fun ProgressBarRemainingEpisodes(tv: TV) {
    // get number of episodes that are not specials
    val seen = tv.seasons.flatMap { it.episodes }.count { it.seasonNumber > 0 && it.seen }
    val total = tv.seasons.flatMap { it.episodes }.count { it.seasonNumber > 0 }

    Text(
        text = "${total - seen} unwatched episodes",
        fontSize = 12.sp,
    )
    LinearProgressIndicator(
        progress = seen.toFloat() / total.toFloat(),
        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
    )
}


/**
 * Format movie length into a readable string.
 *
 * @param time The movie length as a pair of hours and minutes.
 * @return A formatted string representing the movie length.
 */
fun formatMovieLength(time: Pair<Int, Int>): String? {
    if (time.first == 0) {
        return if (time.second == 0) null else time.second.toString() + "min"
    }
    return time.first.toString() + "." + time.second + "h"
}

/**
 * Composable function to display the primary action button in the banner.
 *
 * @param media The media item for which to display the action button.
 * @param dataViewModel The view model containing data and actions related to media items.
 * @param activeBottomNav The active bottom navigation option.
 */
@Composable
fun BannerPrimaryAction(media: Media, dataViewModel: DataViewModel, activeBottomNav: MainNavOption) {
    // Determine and display the appropriate action button based on the active bottom navigation and media status
    if (activeBottomNav == MainNavOption.Search) {
        if (dataViewModel.isInWatchlist(media)) {
            BannerActionButton(media = media, label = "Remove from Watchlist", action = { dataViewModel.moveMediaTo(it, null) }, color = RemoveColor)
        } else {
            BannerActionButton(media = media, label = "Add to Watchlist", action = { dataViewModel.moveMediaTo(it, TopNavOption.ToWatch) }, color = KashmirBlue)
        }
    } else if (media.status == TopNavOption.History) {
        BannerActionButton(media = media, label = "Remove from Watchlist", action = { dataViewModel.moveMediaTo(it, null) }, color = RemoveColor)
    } else if (media is TV && media.status == TopNavOption.ToWatch) {
        BannerActionButton(media = media, label = "Start Watching", action = { dataViewModel.moveMediaTo(it, TopNavOption.Watching) }, color = KashmirBlue)
    } else if (media is TV && media.status == TopNavOption.Watching) {
        // Check for next episode to watch
        val nextEpisode = dataViewModel.getNextUnwatchedEpisode(media)
        if(nextEpisode!=null) {
            NextEpisodeInfo(
                episode = nextEpisode,
                onCheckbox = {dataViewModel.setEpisodeCheckmark(true, media, nextEpisode) }
            )
        } else {    // No more episodes, move to history button
            BannerActionButton(media = media, label = "Seen it!", action = { dataViewModel.moveMediaTo(it, TopNavOption.History) }, color = KashmirBlue)
        }
    }
    else {
        BannerActionButton(media = media, label = "Seen it!", action = { dataViewModel.moveMediaTo(it, TopNavOption.History) }, color = KashmirBlue)
    }
}

/**
 * Composable function to display an action button in the banner.
 *
 * @param media The media item for which to display the action button.
 * @param label The label for the action button.
 * @param action Callback to handle the action button click.
 * @param color The color for the action button.
 */
@Composable
fun BannerActionButton(media: Media, label: String, action: (Media) -> Unit, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { action(media) },
            colors = ButtonDefaults.buttonColors(color),
            modifier = Modifier
                .height(35.dp)
        ) {
            Text(
                text = label,
                fontSize = 13.sp
            )
        }
    }

}