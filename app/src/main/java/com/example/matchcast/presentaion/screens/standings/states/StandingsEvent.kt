package com.example.matchcast.presentaion.screens.standings.states

sealed class StandingsEvent {
    data object EnterScreen : StandingsEvent()

    data object ReloadScreen : StandingsEvent()

    data object OnBackClick : StandingsEvent()

    data class OnTeamClick(val teamName: String) : StandingsEvent()
}
