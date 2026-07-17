package com.example.matchcast.presentaion.screens.favorites.states

sealed class FavoritesEvent {
    data object EnterScreen : FavoritesEvent()

    data object ReloadScreen : FavoritesEvent()

    data object OnBackClick : FavoritesEvent()

    data class OnTeamClick(val teamName: String) : FavoritesEvent()

    data class OnRemoveClick(val teamName: String) : FavoritesEvent()
}
