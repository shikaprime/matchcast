package com.example.matchcast.presentaion.screens.team.states

sealed class TeamAction {
    data object CloseScreen : TeamAction()

    data class NavigateToDetail(val matchId: Int) : TeamAction()

    data class NavigateToTeam(val teamName: String) : TeamAction()
}
