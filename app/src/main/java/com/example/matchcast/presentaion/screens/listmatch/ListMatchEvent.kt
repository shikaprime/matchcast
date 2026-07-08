package com.example.matchcast.presentaion.screens.listmatch

sealed class ListMatchEvent{
    data object ReloadScreen: ListMatchEvent()

    data object EnterScreen: ListMatchEvent()

    data class OnMatchClick(
        val matchId: Int
    ): ListMatchEvent()
}