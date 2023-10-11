package com.example.thewatchlist.data.media

import info.movito.themoviedbapi.model.tv.TvSeries

class TV(var tmdb: TvSeries) : Media(id = tmdb.id) {
    var seasons = {
        tmdb.seasons
    }

}