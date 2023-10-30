package com.example.thewatchlist.data.persistence

import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV
import com.google.gson.Gson

class StorageRepository(db: MediaDb) {
    private val mediaDao = db.mediaDao()

    suspend fun insert(vararg media: Media) {
        media.forEach {
            mediaDao.insert(StoredMedia(it))
        }
    }

    suspend fun delete(vararg media: Media) {
        media.forEach {
            mediaDao.delete(StoredMedia(it))
        }
    }

    suspend fun update(vararg media: Media) {
        media.forEach {
            mediaDao.update(StoredMedia(it))
        }
    }

    suspend fun getMovie(id: Int) : Movie {
        val media = mediaDao.getMovie(id)
        return Gson().fromJson(media.data, Movie::class.java)
    }

    suspend fun getAllMovies() : List<Movie> {
        val media = mediaDao.getAllMovies()
        val gson = Gson()

        //convert to Movie objects
        val movies = listOf<Movie>().toMutableList()
        media.forEach {
            movies.add(
                gson.fromJson(it.data, Movie::class.java)
            )
        }

        return movies.toList()
    }

    suspend fun getSeries(id: Int) : TV {
        val media = mediaDao.getSeries(id)
        return Gson().fromJson(media.data, TV::class.java)
    }

    suspend fun getAllSeries() : List<TV> {
        val media = mediaDao.getAllSeries()
        val gson = Gson()

        //convert to Movie objects
        val series = listOf<TV>().toMutableList()
        media.forEach {
            series.add(
                gson.fromJson(it.data, TV::class.java)
            )
        }

        return series.toList()
    }
}