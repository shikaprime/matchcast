package com.example.matchcast.presentaion.screens.detail.states

sealed class DetailMatchAction{
    data object CloseScreen: DetailMatchAction()

    data class NavigateToTeam(val teamName: String): DetailMatchAction()

}