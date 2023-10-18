package com.example.thewatchlist.data.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StorageDao {
    @Query("SELECT * FROM testentity")
    fun getAll(): List<TestEntity>

    @Query("SELECT * FROM testentity WHERE title = :title")
    fun findByTitle(title: String): List<TestEntity>

    @Insert
    fun insertAll(vararg users: TestEntity)

    @Delete
    fun delete(user: TestEntity)
}
