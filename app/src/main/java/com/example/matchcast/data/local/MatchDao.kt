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
}
