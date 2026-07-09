package com.example.matchcast.presentaion.screens.listmatch.states

import com.example.matchcast.domain.model.Match

sealed class ListMatchState{

    data object Loading: ListMatchState()

    data class Error(
        val icon: Int,
        val description: String
    ): ListMatchState()

    data class Display(
        val listMatch: List<Match> = emptyList()
    ): ListMatchState()

    data class Search(
        val query: String,
        val results: List<Match>,
        val isLoading: Boolean = false,
        val error: String? = null
    ): ListMatchState()
}