package com.example.matchcast.presentaion.screens.listmatch.states

sealed class ListMatchEvent{
    data object ReloadScreen: ListMatchEvent()

    data object EnterScreen: ListMatchEvent()

    data class SearchQueryChanged(val query: String) : ListMatchEvent()

    data object SearchClear : ListMatchEvent()

    data class OnMatchClick(
        val matchId: Int
    ): ListMatchEvent()
}