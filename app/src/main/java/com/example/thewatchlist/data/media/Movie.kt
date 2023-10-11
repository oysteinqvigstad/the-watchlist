package com.example.thewatchlist.data.media

import info.movito.themoviedbapi.model.MovieDb

class Movie(var tmdb: MovieDb) : Media(id = tmdb.id) {


}