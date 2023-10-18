package com.example.thewatchlist.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestEntity (@PrimaryKey val title: String, val year: Int)