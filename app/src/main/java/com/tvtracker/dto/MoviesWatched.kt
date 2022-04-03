package com.tvtracker.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_movies")
class MoviesWatched(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user") var name: String,
    @ColumnInfo(name = "movieId") var movieId: Int
)