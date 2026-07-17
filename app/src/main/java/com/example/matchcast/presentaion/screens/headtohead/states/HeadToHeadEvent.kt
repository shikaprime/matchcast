package com.example.matchcast.presentaion.screens.headtohead.states

sealed class HeadToHeadEvent {
    data class EnterScreen(val teamA: String, val teamB: String) : HeadToHeadEvent()

    data object ReloadScreen : HeadToHeadEvent()

    data object OnBackClick : HeadToHeadEvent()

    data class OnMatchClick(val matchId: Int) : HeadToHeadEvent()
}
