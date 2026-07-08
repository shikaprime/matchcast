package com.example.matchcast.presentaion.screens.listmatch.states

sealed class ListMatchAction {
    data object CloseScreen: ListMatchAction()

    data class NavigateToDetail(
        val matchId: Int
    ): ListMatchAction()
}