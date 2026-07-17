package com.example.matchcast.presentaion.screens.headtohead.states

sealed class HeadToHeadAction {
    data object CloseScreen : HeadToHeadAction()

    data class NavigateToDetail(val matchId: Int) : HeadToHeadAction()
}
