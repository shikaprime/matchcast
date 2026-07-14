package com.example.matchcast.data.remote

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("MatchNumber") val matchNumber: Int,
    @SerializedName("RoundNumber") val roundNumber: Int,
    @SerializedName("DateUtc") val dateUtc: String,
    @SerializedName("Location") val location: String,
    @SerializedName("HomeTeam") val homeTeam: String,
    @SerializedName("AwayTeam") val awayTeam: String,
    @SerializedName("Group") val group: String?,
    @SerializedName("HomeTeamScore") val homeTeamScore: Int,
    @SerializedName("AwayTeamScore") val awayTeamScore: Int,
    @SerializedName("Winner") val winner: String,
)
