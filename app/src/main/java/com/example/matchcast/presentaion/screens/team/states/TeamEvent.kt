package com.example.matchcast.presentaion.screens.team.states

sealed class TeamEvent {
    data class EnterScreen(val teamName: String) : TeamEvent()

    data object ReloadScreen : TeamEvent()

    data object OnBackClick : TeamEvent()

    data class OnMatchClick(val matchId: Int) : TeamEvent()

    data class OnTeamClick(val teamName: String) : TeamEvent()

    data class OnCompareClick(val opponent: String) : TeamEvent()

    data object OnFavoriteClick : TeamEvent()
}
