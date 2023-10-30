package com.example.thewatchlist.data.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {
    // using a default value is a horrible way to do this, but it works for now
    @Query("SELECT * FROM media WHERE media.type = :type")
    suspend fun getAllMovies(type: StoredMediaType = StoredMediaType.Movie) : List<StoredMedia>
    @Query("SELECT * FROM media WHERE media.id = :id AND media.type = :type")
    suspend fun getMovie(id: Int, type: StoredMediaType = StoredMediaType.Movie) : StoredMedia
    @Query("SELECT * FROM media WHERE media.type = :type")
    suspend fun getAllSeries(type: StoredMediaType = StoredMediaType.Tv) : List<StoredMedia>
    @Query("SELECT * FROM media WHERE media.id = :id AND media.type = :type")
    suspend fun getSeries(id: Int, type: StoredMediaType = StoredMediaType.Tv) : StoredMedia
    @Insert
    suspend fun insert(vararg media: StoredMedia)
    @Delete
    suspend fun delete(vararg media: StoredMedia)
    @Update
    suspend fun update(vararg media: StoredMedia)
}