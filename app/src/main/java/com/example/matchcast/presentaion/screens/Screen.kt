package com.example.matchcast.presentaion.screens

import android.net.Uri

object Screen {
    const val Onboarding = "onboarding"
    const val ListMatch = "list_match"
    const val DetailMatch = "detail_match/{matchId}"
    const val Standings = "standings"
    const val TeamDetail = "team_detail/{teamName}"
    const val HeadToHead = "head_to_head/{teamA}/{teamB}"
    const val Favorites = "favorites"
    const val About = "about"
    const val Login = "login"
    const val Signup = "signup"

    fun detailMatchRoute(matchId: Int): String = "detail_match/$matchId"

    fun teamDetailRoute(teamName: String): String = "team_detail/${Uri.encode(teamName)}"

    fun headToHeadRoute(teamA: String, teamB: String): String =
        "head_to_head/${Uri.encode(teamA)}/${Uri.encode(teamB)}"
}
