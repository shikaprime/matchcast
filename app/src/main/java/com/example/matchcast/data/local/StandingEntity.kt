package com.example.matchcast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "standings")
data class StandingEntity(
    @PrimaryKey
    val teamName: String,
    val position: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int
)
