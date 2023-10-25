package com.example.thewatchlist.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.theme.KashmirBlue
import com.example.thewatchlist.ui.theme.RemoveColor


@Composable
fun Banner(
    media: Media,
    dataViewModel: DataViewModel,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
) {

    LaunchedEffect(Unit) {
        if (media is TV) {
            dataViewModel.updateEpisodes(media)
        }
    }

    val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
        .data(media.posterUrl)
        .size(coil.size.Size.ORIGINAL)
        .build()
    )

    ElevatedCard(
        modifier = Modifier
            .padding(6.dp)
            .height(160.dp)
            .clickable { onDetails(media) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Box {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    Text(text = "Image loading...")
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
                            (formatMovieLength(media.runtime)?.let { " \u2022 " + it } ?: "") +
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
                BannerPrimaryAction(media = media, activeBottomNav = activeBottomNav, dataViewModel = dataViewModel)

            }
        }

    }
}




fun formatMovieLength(time: Pair<Int, Int>): String? {
    if (time.first == 0) {
        return if (time.second == 0) null else time.second.toString() + "min"
    }
    return time.first.toString() + "." + time.second + "h"
}


@Composable
fun BannerPrimaryAction(media: Media, dataViewModel: DataViewModel, activeBottomNav: MainNavOption) {
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
    } else {
        BannerActionButton(media = media, label = "Move to History", action = { dataViewModel.moveMediaTo(it, TopNavOption.History) }, color = RemoveColor)
    }
}


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