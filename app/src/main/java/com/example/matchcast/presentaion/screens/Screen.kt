package com.example.matchcast.presentaion.screens

import android.net.Uri

object Screen {
    const val ListMatch = "list_match"
    const val DetailMatch = "detail_match/{matchId}"
    const val Standings = "standings"
    const val TeamDetail = "team_detail/{teamName}"

    fun detailMatchRoute(matchId: Int): String = "detail_match/$matchId"

    fun teamDetailRoute(teamName: String): String = "team_detail/${Uri.encode(teamName)}"
}
