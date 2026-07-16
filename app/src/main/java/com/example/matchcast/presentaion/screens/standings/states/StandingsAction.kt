package com.example.matchcast.presentaion.screens.standings.states

sealed class StandingsAction {
    data object CloseScreen : StandingsAction()

    data class NavigateToTeam(val teamName: String) : StandingsAction()
}
