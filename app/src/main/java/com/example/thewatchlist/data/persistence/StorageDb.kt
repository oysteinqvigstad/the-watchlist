package com.example.thewatchlist.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TestEntity::class], version = 1)
abstract class StorageDb : RoomDatabase() {
    abstract fun storageDao(): StorageDao
}
