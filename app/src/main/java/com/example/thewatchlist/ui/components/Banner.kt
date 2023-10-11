package com.example.thewatchlist.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewatchlist.R
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import info.movito.themoviedbapi.model.Genre


@Composable
fun Banner(
    media: Media,
    activeBottomNav: MainNavOption,
    onDetails: (Media) -> Unit,
    onAdd: (Media) -> Unit
) {

    when (media) {
        is Movie -> MovieBannerTest(
            movie = media,
            activeBottomNav = activeBottomNav,
            onAdd = onAdd,
            onDetails = onDetails
        )
        is TV -> TVBanner(
            tv = media,
            activeBottomNav = activeBottomNav,
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
        Box(modifier = Modifier
            .size(60.dp)
            .clip(RectangleShape)
            .background(Color.Blue))
        Text(text = "Movie: " + movie.tmdb.title, modifier = Modifier.clickable { onDetails(movie) })
        Spacer(modifier = Modifier.padding(start = 2.dp))
        Text(text = "+", modifier = Modifier.clickable { onAdd(movie) })
    }


}

@Composable
fun MovieBannerTest(
    movie: Movie,
    activeBottomNav: MainNavOption,
    onAdd: (Media) -> Unit,
    onDetails: (Media) -> Unit
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
                    text = movie.tmdb.title + getReleaseYear(movie.tmdb.releaseDate),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { onDetails(movie) }
                        .padding(bottom = 5.dp))

                Text(text = getMovieLength(movie.tmdb.runtime), fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 5.dp, start = 10.dp))

                Text(text = getThreeGenres(movie.tmdb.genres), fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 15.dp, start = 10.dp))

                Button(
                    onClick = { onAdd(movie) },
                    colors = ButtonDefaults.buttonColors(Color(65, 106, 145)),
                    modifier = Modifier
                        .height(35.dp)
                        .padding(start = 20.dp)
                ) {

                    Text(
                        text = "Add to watchlist",
                        fontSize = 13.sp
                    )
                }

            }
        }

    }
}


fun getReleaseYear(date: String): String {
 return " (" + date.split("-")[0] + ")"
}

fun getMovieLength(min: Int): String {
    val hour = min/60
    val minute = (min%60).toString()

    if (hour === 0) {
        return minute + "min"
    }

  return hour.toString() + "." + minute + "h"
}

fun getThreeGenres(genres: List<Genre>): String {
    var s = ""
    var genre: String
    var max = 0

    while (max < 3 && max < genres.size) {

        genre = genres[max].toString().split("[")[0].trim()
        s += "$genre, "
        max++
    }

    return s.trim().trim(',')
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
