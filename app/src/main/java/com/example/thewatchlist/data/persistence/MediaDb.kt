package com.example.thewatchlist.data.persistence

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV

@Database(entities = [StoredMedia::class], version = 2)
abstract class MediaDb : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}

object MediaDatabase {
    private lateinit var instance : MediaDb
    private var initialized = false

    fun getInstance() : MediaDb {
        return instance
    }

    fun init(context: Context) {
        // init database only once
        if(initialized) {
            Log.w("MediaDatabase", "Multiple calls to init MediaDb.")
            return
        }

        instance = Room.databaseBuilder(
            context,
            MediaDb::class.java, "database-name"
        ).fallbackToDestructiveMigration()
            .build()

        initialized = true
    }
}