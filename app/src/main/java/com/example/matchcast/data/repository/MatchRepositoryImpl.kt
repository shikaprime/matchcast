package com.example.matchcast.data.repository

import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.remote.RetrofitClient
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.repository.MatchRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao
): MatchRepository {

    override fun getMatches(): Flow<List<Match>> {
        return matchDao.getMatches().map { entities ->
            entities.map { it.toDomain() }
        }
    }


    override suspend fun refreshMatch() {
        coroutineScope {
            val matches = RetrofitClient.apiService.getMatches()

            val matchesEntity = mutableListOf<MatchEntity>()
            matches.forEach {
                matchesEntity.add(
                    MatchEntity(
                        matchNumber = it.matchNumber,
                        roundNumber = it.roundNumber,
                        dateUtc = it.dataUtc,
                        location = it.location,
                        homeTeam = it.homeTeam,
                        awayTeam = it.awayTeam,
                        group = it.group,
                        homeTeamScore = it.homeTeamScore,
                        awayTeamScore = it.awayTeamScore,
                        winner = it.winner
                    )
                )
            }
            matchDao.insertMatches(matchesEntity)
        }
    }

    override fun searchMatches(query: String): Flow<List<Match>> {
        return matchDao.searchMatches(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMatch(id: Int): Flow<Match> {
        return matchDao.getMatch(id).map { entity ->
            entity.toDomain()
        }
    }

    private fun MatchEntity.toDomain(): Match {
        val formattedDate = try {
            val utcDateTime = ZonedDateTime.parse(this.dateUtc)
            val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())
            localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } catch (e: Exception) {
            this.dateUtc
        }

        return Match(
            matchNumber = this.matchNumber,
            roundNumber = this.roundNumber,
            formateDateUtc = formattedDate,
            location = this.location,
            homeTeam = this.homeTeam,
            awayTeam = this.awayTeam,
            group = this.group,
            homeTeamScore = this.homeTeamScore,
            awayTeamScore = this.awayTeamScore,
            winner = this.winner
        )
    }
}
