package com.example.matchcast.presentaion.screens.detail

import com.example.matchcast.domain.model.Match

sealed class DetailMatchViewState{
    data object Loading: DetailMatchViewState()

    data class Error(
        val icon: Int,
        val description: String
    ): DetailMatchViewState()

    data class Display(
        val match: Match
    ): DetailMatchViewState()
}