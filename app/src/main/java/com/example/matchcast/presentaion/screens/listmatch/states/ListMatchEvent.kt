package com.example.matchcast.presentaion.screens.listmatch.states

sealed class ListMatchEvent {
    data object EnterScreen : ListMatchEvent()

    data object ReloadScreen : ListMatchEvent()

    data object SearchActivate : ListMatchEvent()

    data class SearchQueryChanged(val query: String) : ListMatchEvent()

    data object SearchClear : ListMatchEvent()

    data class FilterOptionSelected(
        val filterType: FilterType,
        val value: String
    ) : ListMatchEvent()

    data object ClearFilters : ListMatchEvent()

    data class OnMatchClick(val matchId: Int) : ListMatchEvent()

    data class OnTeamClick(val teamName: String) : ListMatchEvent()

    data object OnStandingsClick : ListMatchEvent()

    data object OnFavoritesClick : ListMatchEvent()

    data object OnAboutClick : ListMatchEvent()

    data object OnAccountClick : ListMatchEvent()
}
