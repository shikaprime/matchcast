package com.example.matchcast.presentaion.screens.detail.states

import com.example.matchcast.domain.model.Match

sealed class DetailMatchState{
    data object Loading: DetailMatchState()

    data class Error(
        val icon: Int,
        val description: String
    ): DetailMatchState()

    data class Display(
        val match: Match
    ): DetailMatchState()
}