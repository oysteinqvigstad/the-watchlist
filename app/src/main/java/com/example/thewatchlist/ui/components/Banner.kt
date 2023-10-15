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
import com.example.thewatchlist.ui.DataViewModel
import info.movito.themoviedbapi.model.Genre


@Composable
fun Banner(
    media: Media,
    dataViewModel: DataViewModel,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
    onAdd: (Media) -> Unit,
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
            onAdd = onAdd,
            onDetails = onDetails
        )
    }
}

@Composable
fun TVBanner(
    tv: TV,
    onAdd: (Media) -> Unit,
    onDetails: (Media) -> Unit
) {
    Row {
        Text(text = "TV: " + tv.title, modifier = Modifier.clickable { onDetails(tv) })
        Spacer(modifier = Modifier.padding(start = 2.dp))
        Text(text = "+", modifier = Modifier.clickable { onAdd(tv) })
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

                BannerPrimaryActionButton(media = movie, dataViewModel = dataViewModel, activeNavOption = activeBottomNav)
            }
        }

    }
}

@Composable
fun BannerPrimaryActionButton(
    media: Media,
    activeNavOption: MainNavOption,
    dataViewModel: DataViewModel
) {
    val contents = media.getPrimaryAction(activeNavOption = activeNavOption, dataViewModel = dataViewModel)


    Button(
        onClick = { contents.action(media) },
        colors = ButtonDefaults.buttonColors(contents.color),
        modifier = Modifier
            .height(35.dp)
            .padding(start = 20.dp)
    ) {
        Text(
            text = contents.label,
            fontSize = 13.sp
        )
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
