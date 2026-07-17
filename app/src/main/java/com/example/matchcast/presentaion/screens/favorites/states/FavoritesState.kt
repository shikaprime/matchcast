package com.example.matchcast.presentaion.screens.favorites.states

import com.example.matchcast.domain.model.FavoriteTeam

sealed class FavoritesState {

    data object Loading : FavoritesState()

    data class Error(
        val icon: Int,
        val description: String
    ) : FavoritesState()

    data class Display(
        val favorites: List<FavoriteTeam> = emptyList()
    ) : FavoritesState()
}
