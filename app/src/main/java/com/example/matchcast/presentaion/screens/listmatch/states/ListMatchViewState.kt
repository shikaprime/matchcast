package com.example.matchcast.presentaion.screens.listmatch.states

import androidx.room.Query
import com.example.matchcast.domain.model.Match

sealed class ListMatchViewState{

    data object Loading: ListMatchViewState()

    data class Error(
        val icon: Int,
        val description: String
    ): ListMatchViewState()

    data class Display(
        val listMatch: List<Match> = emptyList()
    ): ListMatchViewState()

    data class Search(
        val query: String,
        val results: List<Match>,
        val isLoading: Boolean = false,
        val error: String? = null
    ): ListMatchViewState()
}