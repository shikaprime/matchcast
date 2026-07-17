package com.example.matchcast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTeamDao {
    @Query("select * from favorite_teams order by addedAt desc")
    fun getFavorites(): Flow<List<FavoriteTeamEntity>>

    @Query("select exists(select 1 from favorite_teams where teamName = :teamName)")
    fun isFavorite(teamName: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteTeamEntity)

    @Query("delete from favorite_teams where teamName = :teamName")
    suspend fun delete(teamName: String)
}
