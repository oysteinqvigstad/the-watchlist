package com.example.thewatchlist.data.persistence

import androidx.room.Entity
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV
import com.google.gson.Gson

@Entity(tableName = "media", primaryKeys = ["id", "type"])
data class StoredMedia(
    val id: Int,
    val type: StoredMediaType,
    val data: String
) {
    constructor(movie: Movie) : this(
        id = movie.id,
        type = StoredMediaType.Movie,
        data = Gson().toJson(movie)
    )

    constructor(series: TV) : this(
        id = series.id,
        type = StoredMediaType.Tv,
        data = Gson().toJson(series)
    )

    constructor(media: Media) : this(
        id = media.id,
        type = when (media) {
            is Movie -> StoredMediaType.Movie
            else -> StoredMediaType.Tv
        },
        data = when (media) { // use when statement to convert type of media
            is Movie -> Gson().toJson(media)
            is TV -> Gson().toJson(media)
            else -> ""
        }
    )
}

enum class StoredMediaType {
    Movie,
    Tv
}
