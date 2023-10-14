package com.example.thewatchlist.data.media

import android.util.Log
import com.example.thewatchlist.data.navigation.MainNavItem
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.MainNavState
import com.example.thewatchlist.ui.theme.KashmirBlue
import com.example.thewatchlist.ui.theme.RemoveColor
import info.movito.themoviedbapi.model.MovieDb

class Movie(var tmdb: MovieDb) : Media(id = tmdb.id) {

    public override fun clone(): Movie {
        return super.clone() as Movie
    }

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

}