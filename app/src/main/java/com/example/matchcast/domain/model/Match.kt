package com.example.matchcast.domain.model

data class Match (
    val matchNumber: Int,
    val roundNumber: Int,
    val formateDateUtc: String,
    val location: String,
    val homeTeam: String,
    val awayTeam: String,
    val group: String?,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val winner: String,
    val homeTeamForm: List<MatchOutcome> = emptyList(),
    val awayTeamForm: List<MatchOutcome> = emptyList()
)