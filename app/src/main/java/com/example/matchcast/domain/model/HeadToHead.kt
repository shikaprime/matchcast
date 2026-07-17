package com.example.matchcast.domain.model

data class HeadToHead(
    val teamA: String,
    val teamB: String,
    val teamAWins: Int,
    val teamBWins: Int,
    val draws: Int,
    val teamAGoals: Int,
    val teamBGoals: Int,
    val matches: List<Match>
) {
    val totalMatches: Int get() = matches.size
}
