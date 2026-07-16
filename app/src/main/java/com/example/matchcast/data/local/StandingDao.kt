package com.example.matchcast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingDao {
    @Query("SELECT * FROM standings ORDER BY position ASC")
    fun getStandings(): Flow<List<StandingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(standings: List<StandingEntity>)

    @Query("DELETE FROM standings")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(standings: List<StandingEntity>) {
        clearAll()
        insertAll(standings)
    }
}
