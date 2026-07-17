package com.example.matchcast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Query("select * from matches")
    fun getMatches(): Flow<List<MatchEntity>>

    @Query("select * from matches where matchNumber = :id ")
    fun getMatch(id: Int): Flow<MatchEntity>

    @Query("select * from matches where awayTeam like :query or homeTeam like :query")
    fun searchMatches(query: String): Flow<List<MatchEntity>>

    @Query("select * from matches where homeTeam = :teamName or awayTeam = :teamName order by dateUtc desc")
    fun getMatchesForTeam(teamName: String): Flow<List<MatchEntity>>

    @Query(
        "select * from matches where (homeTeam = :teamA and awayTeam = :teamB) " +
            "or (homeTeam = :teamB and awayTeam = :teamA) order by dateUtc desc"
    )
    fun getMatchesBetweenTeams(teamA: String, teamB: String): Flow<List<MatchEntity>>
}
