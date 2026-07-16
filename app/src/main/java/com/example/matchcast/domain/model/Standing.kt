package com.example.matchcast.domain.model

data class Standing(
    val position: Int,
    val teamName: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int
) {
    val goalDifference: Int get() = goalsFor - goalsAgainst
}
