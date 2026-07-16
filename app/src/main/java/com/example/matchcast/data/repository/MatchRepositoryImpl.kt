package com.example.matchcast.data.repository

import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.local.StandingDao
import com.example.matchcast.data.local.StandingEntity
import com.example.matchcast.data.remote.RetrofitClient
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.domain.model.Standing
import com.example.matchcast.domain.repository.MatchRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao,
    private val standingDao: StandingDao
): MatchRepository {

    override fun getMatches(): Flow<List<Match>> {
        return matchDao.getMatches().map { entities ->
            val totalFormMap = calculateTeamMatch(entities)
            entities.map {
                it.toDomain(totalFormMap)
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
                        dateUtc = it.dateUtc,
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
            standingDao.replaceAll(calculateStandings(matchesEntity))
        }
    }

    override fun searchMatches(query: String): Flow<List<Match>> {
        return matchDao.searchMatches(query).map { entities ->
            val totalFormMap = calculateTeamMatch(entities)
            entities.map { it.toDomain(totalFormMap) }
        }
    }

    override fun getMatch(id: Int): Flow<Match> {
        return combine(
            matchDao.getMatch(id),
            matchDao.getMatches()
        ) { entity, allEntities ->
            val totalFormMap = calculateTeamMatch(allEntities)
            entity.toDomain(totalFormMap)
        }
    }

    override fun getStandings(): Flow<List<Standing>> {
        return standingDao.getStandings().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getTeamMatches(teamName: String): Flow<List<Match>> {
        return combine(
            matchDao.getMatchesForTeam(teamName),
            matchDao.getMatches()
        ) { teamEntities, allEntities ->
            val totalFormMap = calculateTeamMatch(allEntities)
            teamEntities.map { it.toDomain(totalFormMap) }
        }
    }

    private fun MatchEntity.toDomain(formMap: Map<String, ArrayDeque<MatchOutcome>>): Match {
        val formattedDate = try {
            val utcDateTime = ZonedDateTime.parse(this.dateUtc)
            val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())
            localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return Match(
            matchNumber = this.matchNumber,
            roundNumber = this.roundNumber,
            formattedDateUtc = formattedDate,
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

    private fun StandingEntity.toDomain(): Standing {
        return Standing(
            position = this.position,
            teamName = this.teamName,
            played = this.played,
            wins = this.wins,
            draws = this.draws,
            losses = this.losses,
            goalsFor = this.goalsFor,
            goalsAgainst = this.goalsAgainst,
            points = this.points
        )
    }

    private class StandingAccumulator {
        var played = 0
        var wins = 0
        var draws = 0
        var losses = 0
        var goalsFor = 0
        var goalsAgainst = 0
        val points get() = wins * 3 + draws
    }

    private fun calculateStandings(matches: List<MatchEntity>): List<StandingEntity> {
        val table = mutableMapOf<String, StandingAccumulator>()

        for (match in matches) {
            val home = table.getOrPut(match.homeTeam) { StandingAccumulator() }
            val away = table.getOrPut(match.awayTeam) { StandingAccumulator() }

            home.played++
            away.played++
            home.goalsFor += match.homeTeamScore
            home.goalsAgainst += match.awayTeamScore
            away.goalsFor += match.awayTeamScore
            away.goalsAgainst += match.homeTeamScore

            when {
                match.homeTeamScore > match.awayTeamScore -> {
                    home.wins++
                    away.losses++
                }
                match.homeTeamScore < match.awayTeamScore -> {
                    away.wins++
                    home.losses++
                }
                else -> {
                    home.draws++
                    away.draws++
                }
            }
        }

        return table.entries
            .sortedWith(
                compareByDescending<Map.Entry<String, StandingAccumulator>> { it.value.points }
                    .thenByDescending { it.value.goalsFor - it.value.goalsAgainst }
                    .thenByDescending { it.value.goalsFor }
                    .thenBy { it.key }
            )
            .mapIndexed { index, (team, acc) ->
                StandingEntity(
                    teamName = team,
                    position = index + 1,
                    played = acc.played,
                    wins = acc.wins,
                    draws = acc.draws,
                    losses = acc.losses,
                    goalsFor = acc.goalsFor,
                    goalsAgainst = acc.goalsAgainst,
                    points = acc.points
                )
            }
    }

    private fun calculateTeamMatch(matches: List<MatchEntity>): Map<String, ArrayDeque<MatchOutcome>> {

        val formMap = mutableMapOf<String, ArrayDeque<MatchOutcome>>()

        for (match in matches) {
            val homeTeam = formMap.getOrPut(match.homeTeam) { ArrayDeque(5) }
            if (homeTeam.size == 5) homeTeam.removeFirst()
            homeTeam.addLast(determineOutcome(match, true))

            val awayTeam = formMap.getOrPut(match.awayTeam) { ArrayDeque(5) }
            if (awayTeam.size == 5) awayTeam.removeFirst()
            awayTeam.addLast(determineOutcome(match, false))
        }

        return formMap

    }

    private fun determineOutcome(match: MatchEntity, isHome: Boolean): MatchOutcome {
        return when {
            match.awayTeamScore == match.homeTeamScore -> MatchOutcome.DRAW
            isHome && match.homeTeamScore > match.awayTeamScore -> MatchOutcome.WIN
            !isHome && match.awayTeamScore > match.homeTeamScore -> MatchOutcome.WIN
            else -> {
                MatchOutcome.LOSE
            }
        }
    }
}
