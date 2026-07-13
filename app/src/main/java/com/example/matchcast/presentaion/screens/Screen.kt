package com.example.matchcast.presentaion.screens

object Screen {
    const val ListMatch = "list_match"
    const val DetailMatch = "detail_match/{matchId}"

    fun detailMatchRoute(matchId: Int): String = "detail_match/$matchId"
}