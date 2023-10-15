package com.example.thewatchlist.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewatchlist.R
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
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

    when (media) {
        is Movie -> MovieBannerTest(
            movie = media,
            dataViewModel = dataViewModel,
            activeBottomNav = activeBottomNav,
            onDetails = onDetails,
        )
        is TV -> TVBanner(
            tv = media,
            dataViewModel = dataViewModel,
            onDetails = onDetails
        )
    }
}

@Composable
fun TVBanner(
    tv: TV,
    dataViewModel: DataViewModel,
    onDetails: (Media) -> Unit
) {
    Row {
        Text(text = "TV: " + tv.title, modifier = Modifier.clickable { onDetails(tv) })
        Spacer(modifier = Modifier.padding(start = 2.dp))
        Text(text = "+", modifier = Modifier.clickable { dataViewModel.moveMediaTo(tv, TopNavOption.ToWatch) })
    }
}




@Composable
fun MovieBannerTest(
    movie: Movie,
    dataViewModel: DataViewModel,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
) {
    Card(modifier = Modifier.padding(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Image(painter = painterResource(R.drawable.testbilde),
                contentDescription = "Film poster",
                modifier = Modifier
                    .width(125.dp)
                    .height(180.dp))

            Spacer(modifier = Modifier.padding(start = 20.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                Text(
                    text = movie.title + "(" + movie.releaseYear.toString() + ")",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { onDetails(movie) }
                        .padding(bottom = 5.dp))

                Text(text = formatMovieLength(movie.runtime),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 5.dp, start = 10.dp))

                Text(text = movie.genres.take(3).joinToString(", ") { it.name },
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 15.dp, start = 10.dp))

                BannerPrimaryAction(media = movie, activeBottomNav = activeBottomNav, dataViewModel = dataViewModel)
            }
        }

    }
}




fun formatMovieLength(time: Pair<Int, Int>): String {
    if (time.first == 0) {
        return time.second.toString() + "min"
    }
    return time.first.toString() + "." + time.second + "h"
}


@Preview(showBackground = true)
@Composable
fun PreviewMovie() {

    Card(modifier = Modifier.padding(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Image(painter = painterResource(R.drawable.testbilde),
                contentDescription = "Film poster",
                modifier = Modifier
                    .width(125.dp)
                    .height(180.dp)
                    .padding(5.dp))

            Spacer(modifier = Modifier.padding(start = 20.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {

                Text(text = "Avatar" , fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "2.45h", fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 5.dp, start = 10.dp))
                Text(text = "Adventure", fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 15.dp, start = 10.dp))

                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color(65, 106, 145)),
                    modifier = Modifier
                        .height(35.dp)
                        .padding(start = 50.dp)) {

                    Text(text = "Add to watchlist",
                        fontSize = 10.sp)
                }
            }
        }

    }
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