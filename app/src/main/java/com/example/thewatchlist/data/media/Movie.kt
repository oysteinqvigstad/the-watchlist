package com.example.thewatchlist.data.media
/*
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.theme.RemoveColor
import info.movito.themoviedbapi.model.Genre
import info.movito.themoviedbapi.model.MovieDb

class Movie(
    id: Int,
    title: String,
    overview: String,
    genres: List<Genre>,
    releaseYear: Int,
    val runtime: Pair<Int, Int>,
) : Media(
    id = id,
    title = title,
    overview = overview,
    genres = genres,
    releaseYear = releaseYear
), Cloneable {

    override fun clone(): Movie {
        return super<Media>.clone() as Movie
    }


    constructor(tmdb: MovieDb) : this(
        id = tmdb.id,
        title = tmdb.title,
        overview = tmdb.overview,
        genres = tmdb.genres,
        releaseYear = tmdb.releaseDate.split("-")[0].toIntOrNull() ?: 0,
        runtime = Pair(tmdb.runtime / 60, tmdb.runtime % 60)

    )

// TODO: reimplement
/*
    override fun getPrimaryAction(
        activeNavOption: MainNavOption,
        dataViewModel: DataViewModel
    ): Button {
        if (activeNavOption == MainNavOption.Search) {
            return super.getPrimaryAction(activeNavOption, dataViewModel)
        } else {
            return if (this.status == TopNavOption.ToWatch) {
                Button(
                    label = "Move to History",
                    action = { dataViewModel.moveToHistory(this) },
                    color = RemoveColor
                )
            } else {
                Button(
                    label = "Remove from Watchlist",
                    action = { dataViewModel.removeMediaFromList(this) },
                    color = RemoveColor
                )
            }
        }
    }
*/
}

 */