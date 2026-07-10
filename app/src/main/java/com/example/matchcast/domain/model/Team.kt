package com.example.matchcast.domain.model

data class Team(
    val name: String,
    val logoUrl: String = "",
    val stats: String = "",
    val form: List<MatchOutcome> = emptyList()
)
