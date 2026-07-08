package com.example.matchcast.presentaion.screens.detail.states

sealed class DetailMatchEvent{
    data class EnterScreen(val matchId: Int): DetailMatchEvent()

    data object ReloadScreen: DetailMatchEvent()

}