package com.example.thewatchlist.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.screens.LoadingIndicator
import com.example.thewatchlist.ui.theme.KashmirBlue
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                val showNotify = activeBottomNav != MainNavOption.Search && media.notify
                TitleHeader(media, showNotify)

                if (media is TV && activeBottomNav != MainNavOption.Search) {
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
 *  Composable function for displaying the title header and notification text
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleHeader(
    media: Media,
    showNotify: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = media.title,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (showNotify) {
            Badge {
                Text(text = media.notifyText)
            }
        }

    }

}

/**
 * Show up to three genres on a single line
 */
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

/**
 * Composable function that displays a progress bar of episodes seen
 */
@Composable
fun ProgressBarRemainingEpisodes(tv: TV) {
    // get number of episodes that are not specials
    val seen = tv.seasons.flatMap { it.episodes }.count { it.seasonNumber > 0 && it.seen }
    val total = tv.seasons.flatMap { it.episodes }.count { it.seasonNumber > 0 }

    Text(
        text = "${total - seen} unwatched episodes",
        fontSize = 13.sp,
        modifier = Modifier.padding(top = 5.dp)
    )
    LinearProgressIndicator(
        progress = seen.toFloat() / total.toFloat(),
        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
        color = KashmirBlue
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        if (activeBottomNav == MainNavOption.Search) {
            if (dataViewModel.isInWatchlist(media)) {
                BannerButton(icon = Icons.Filled.Star, description = "Remove from watchlist") {
                    dataViewModel.moveMediaTo(media, null)
                }
            } else {
                BannerButton(icon = Icons.Filled.StarBorder, description = "Add to watchlist") {
                    dataViewModel.moveMediaTo(media, TopNavOption.ToWatch)
                }
            }
        } else if (media.status == TopNavOption.History) {
            if (media is TV) {
                BannerButton(icon = Icons.Filled.Refresh, description = "Rewatch") {
                    dataViewModel.moveMediaTo(media, TopNavOption.ToWatch)
                }
                Spacer(modifier = Modifier.padding(start = 5.dp))
            }
            BannerButton(icon = Icons.Filled.Delete, description = "delete", onClick = {dataViewModel.moveMediaTo(media, null) })
        } else if (media is TV && media.status == TopNavOption.ToWatch) {
            BannerTextButton(icon = Icons.Filled.PlayArrow, label = "Start Tracking") {
                dataViewModel.moveMediaTo(media, TopNavOption.Watching)
            }
        } else if (media is TV && media.status == TopNavOption.Watching) {
            // Check for next episode to watch
            val nextEpisode = dataViewModel.getNextUnwatchedEpisode(media)
            if(nextEpisode!=null) {
                BannerTextButton(icon = Icons.Filled.Done, label =
                String.format("S%02dE%02d", nextEpisode.seasonNumber, nextEpisode.episodeNumber)) {
                    dataViewModel.setEpisodeCheckmark(true, media, nextEpisode)
                }
                Spacer(Modifier.weight(1f))
            }

            BannerButton(icon = Icons.Filled.Inventory, description = "seen it") {
                dataViewModel.moveMediaTo(media, TopNavOption.History)
            }
        }
        else {
            BannerButton(icon = Icons.Filled.Check, description = "Seen it", onClick = { dataViewModel.moveMediaTo(media, TopNavOption.History) })
        }

    }
}

/**
 * Composable function of a button with icon
 */
@Composable
fun BannerButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    FilledTonalIconButton(
        onClick = { onClick() },
        modifier = Modifier.padding(top = 13.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = KashmirBlue
        )
    }
}

/**
 * Composable function of a button with icon and text label
 */
@Composable
fun BannerTextButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = { onClick() },
        modifier = Modifier.padding(top = 13.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = " $label",
            tint = KashmirBlue
        )
        Text(text = label, color = KashmirBlue)
    }
}