package com.example.matchcast.presentaion.screens.favorites.states

sealed class FavoritesAction {
    data object CloseScreen : FavoritesAction()

    data class NavigateToTeam(val teamName: String) : FavoritesAction()
}
