package com.example.matchcast.presentaion.screens.standings.states

import com.example.matchcast.domain.model.Standing

sealed class StandingsState {

    data object Loading : StandingsState()

    data class Error(
        val icon: Int,
        val description: String
    ) : StandingsState()

    data class Display(
        val standings: List<Standing> = emptyList(),
        val favoriteTeamNames: Set<String> = emptySet()
    ) : StandingsState()
}
