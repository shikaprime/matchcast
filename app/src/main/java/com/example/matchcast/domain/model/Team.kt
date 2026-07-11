package com.example.matchcast.domain.model

data class Team(
    val name: String,
    val logoUrl: String = "",
    val form: List<MatchOutcome> = emptyList()
)
