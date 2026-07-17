package com.example.matchcast.presentaion.screens.team.states

import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome

sealed class TeamState {

    data object Loading : TeamState()

    data class Error(
        val icon: Int,
        val description: String
    ) : TeamState()

    data class Display(
        val teamName: String,
        val fullName: String,
        val stadium: String,
        val form: ArrayDeque<MatchOutcome>?,
        val recentMatches: List<Match> = emptyList(),
        val isFavorite: Boolean = false
    ) : TeamState()
}
