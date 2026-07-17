package com.example.matchcast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_teams")
data class FavoriteTeamEntity(
    @PrimaryKey
    val teamName: String,
    val addedAt: Long
)
