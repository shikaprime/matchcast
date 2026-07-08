package com.example.matchcast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(true)
    val matchNumber: Int = 0,
    val roundNumber: Int,
    val dateUtc: String,
    val location: String,
    val homeTeam: String,
    val awayTeam: String,
    val group: String?,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val winner: String
)
