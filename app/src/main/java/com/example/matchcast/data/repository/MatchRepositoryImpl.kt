package com.example.matchcast.data.repository

import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.remote.RetrofitClient
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.domain.repository.MatchRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao
): MatchRepository {

    override fun getMatches(): Flow<List<Match>> {
        return matchDao.getMatches().map { entities ->
            val totalFormMap = calculateTeamMatch(entities)
            entities.map { it.toDomain(totalFormMap)
            }
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
            val totalFormMap = calculateTeamMatch(entities)
            entities.map { it.toDomain(totalFormMap) }
        }
    }

    override fun getMatch(id: Int): Flow<Match> {
        return matchDao.getMatch(id).map { entity ->
            entity.toDomain(emptyMap())
        }
    }

    private fun MatchEntity.toDomain(formMap: Map<String, ArrayDeque<MatchOutcome>>): Match {
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
            winner = this.winner,
            homeTeamForm = formMap[this.homeTeam],
            awayTeamForm = formMap[this.awayTeam]
        )
    }

    private fun calculateTeamMatch(matches :List<MatchEntity>): Map<String, ArrayDeque<MatchOutcome>>{

        val formMap = mutableMapOf<String, ArrayDeque<MatchOutcome>>()

        for (match in matches){
            val homeTeam = formMap.getOrPut(match.homeTeam){ ArrayDeque(5) }
            if (homeTeam.size==5) homeTeam.removeFirst()
            homeTeam.addLast(determineOutcome(match,true))

            val awayTeam = formMap.getOrPut(match.awayTeam){ ArrayDeque(5) }
            if (awayTeam.size ==5 ) awayTeam.removeFirst()
            awayTeam.addLast(determineOutcome(match,false))
        }

        return formMap

    }

    private fun determineOutcome(match: MatchEntity,isHome: Boolean): MatchOutcome{
        return when{
            match.awayTeamScore==match.homeTeamScore -> MatchOutcome.DRAW
            isHome && match.homeTeamScore > match.awayTeamScore -> MatchOutcome.WIN
            !isHome && match.awayTeamScore > match.homeTeamScore -> MatchOutcome.WIN
            else -> { MatchOutcome.LOSE }
        }
    }
}
