package com.example.matchcast.presentaion.screens.detail

sealed class DetailMatchEvent{
    data object EnterScreen: DetailMatchEvent()

    data object ReloadScreen: DetailMatchEvent()

}