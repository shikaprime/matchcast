package com.example.matchcast.domain.repository

import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.Standing
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    fun getMatches(): Flow<List<Match>>

    suspend fun refreshMatch()

    fun searchMatches(query: String): Flow<List<Match>>

    fun getMatch(id: Int): Flow<Match>

    fun getStandings(): Flow<List<Standing>>

    fun getTeamMatches(teamName: String): Flow<List<Match>>
}
