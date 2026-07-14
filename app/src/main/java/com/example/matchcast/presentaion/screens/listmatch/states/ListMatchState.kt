package com.example.matchcast.presentaion.screens.listmatch.states

import com.example.matchcast.domain.model.Match

sealed class ListMatchState {

    data object Loading : ListMatchState()

    data class Error(
        val icon: Int,
        val description: String
    ) : ListMatchState()

    data class Display(
        val matches: List<Match> = emptyList(),
        val searchQuery: String = "",
        val isSearchActive: Boolean = false,
        val availableFilters: Map<FilterType, List<String>> = emptyMap(),
        val activeFilters: Map<FilterType, String> = emptyMap()
    ) : ListMatchState()
}
