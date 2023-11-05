package com.example.thewatchlist.ui.components


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
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
import com.example.thewatchlist.ui.screens.ErrorMessage
import com.example.thewatchlist.ui.screens.LoadingIndicator
import com.example.thewatchlist.ui.screens.SearchScreen
import com.example.thewatchlist.ui.theme.KashmirBlue
import com.example.thewatchlist.ui.theme.RemoveColor

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
//            dataViewModel.updateEpisodes(media)
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

            Box (modifier = Modifier.width(110.dp).fillMaxHeight()){
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
            Column(modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
            ) {

                Text(
                    text = media.title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 5.dp)
                )
                Text(
                    text = media.releaseYear.let { if (it == 0) "TBA" else it.toString() } +
                            (formatMovieLength(media.runtime)?.let { " \u2022 $it" } ?: "") +
                            if (media is TV) " \u2022 " + media.numberOfEpisodes + " episodes" else ""

                    ,
                    fontSize = 12.sp,
                )

                Text(text = media.genres.take(3).joinToString(", ") { it.name },
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.padding(top = 25.dp))

                // Display primary action button based on the active bottom navigation and media status
                BannerPrimaryAction(media = media, activeBottomNav = activeBottomNav, dataViewModel = dataViewModel)

            }
        }

    }
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
        var nextEpisode = dataViewModel.getNextUnwatchedEpisode(media)
        if(nextEpisode!=null) {
            BannerEpisodeCheck(media, nextEpisode, dataViewModel)
        } else {    // No more episodes, move to history button
            BannerActionButton(media = media, label = "Move to History", action = { dataViewModel.moveMediaTo(it, TopNavOption.History) }, color = RemoveColor)
        }
    }
    else {
        BannerActionButton(media = media, label = "Move to History", action = { dataViewModel.moveMediaTo(it, TopNavOption.History) }, color = RemoveColor)
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
    Button(
        onClick = { action(media) },
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier
            .height(35.dp)
            .padding(start = 20.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp
        )
    }
}

/**
 * Function to check off the next episode in a series as watched from the banner.
 */
@Composable
fun BannerEpisodeCheck(tv: TV, episode: Episode, dataViewModel: DataViewModel) {
    var checked = false
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 20.dp)
    )
    {
        Text(
            text = String.format("S%02dE%02d", episode.seasonNumber, episode.episodeNumber)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = !checked;
                dataViewModel.setEpisodeCheckmark(true, tv, episode)
            }
        )
    }
}