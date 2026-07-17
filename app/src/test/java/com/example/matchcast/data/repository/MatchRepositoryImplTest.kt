package com.example.matchcast.data.repository

import com.example.matchcast.data.local.FavoriteTeamDao
import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.local.StandingDao
import com.example.matchcast.data.local.StandingEntity
import com.example.matchcast.data.remote.RetrofitClient
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.domain.model.Standing
import com.example.matchcast.domain.repository.MatchRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.ArrayDeque
import kotlin.collections.emptyList

class MatchRepositoryImplTest {
    private lateinit var matchRepository: MatchRepository
    private lateinit var matchDao: MatchDao
    private lateinit var standingDao: StandingDao
    private lateinit var favoriteTeamDao: FavoriteTeamDao


    object TestData {

        const val DATE_RAW_1 = "2023-08-11 19:00:00Z"
        const val DATE_RAW_2 = "2023-08-12 15:30:00Z"

        const val DATE_FORMATTED_1 = "11.08.2023 22:00"
        const val DATE_FORMATTED_2 = "12.08.2023 18:30"

        val sampleMatchEntity1 = MatchEntity(
            matchNumber = 1,
            roundNumber = 1,
            dateUtc = DATE_RAW_1,
            location = "Old Trafford",
            homeTeam = "Man United",
            awayTeam = "Wolves",
            group = "Group A",
            homeTeamScore = 1,
            awayTeamScore = 0,
            winner = "Man United"
        )

        val sampleMatchEntity2 = MatchEntity(
            matchNumber = 2,
            roundNumber = 1,
            dateUtc = DATE_RAW_2,
            location = "Emirates Stadium",
            homeTeam = "Arsenal",
            awayTeam = "Nottingham Forest",
            group = "Group A",
            homeTeamScore = 2,
            awayTeamScore = 1,
            winner = "Arsenal"
        )

        val matchEntitiesList = listOf(sampleMatchEntity1, sampleMatchEntity2)
        val sampleMatch1 = Match(
            matchNumber = 1,
            roundNumber = 1,
            formattedDateUtc = DATE_FORMATTED_1,
            location = "Old Trafford",
            homeTeam = "Man United",
            awayTeam = "Wolves",
            group = "Group A",
            homeTeamScore = 1,
            awayTeamScore = 0,
            winner = "Man United",
            homeTeamForm = ArrayDeque(listOf(MatchOutcome.WIN)),
            awayTeamForm = ArrayDeque(listOf(MatchOutcome.LOSE))
        )

        val sampleMatch2 = Match(
            matchNumber = 2,
            roundNumber = 1,
            formattedDateUtc = DATE_FORMATTED_2,
            location = "Emirates Stadium",
            homeTeam = "Arsenal",
            awayTeam = "Nottingham Forest",
            group = "Group A",
            homeTeamScore = 2,
            awayTeamScore = 1,
            winner = "Arsenal",
            homeTeamForm = ArrayDeque(listOf(MatchOutcome.WIN)),
            awayTeamForm = ArrayDeque(listOf(MatchOutcome.LOSE))
        )

        val domainMatchesList = listOf(sampleMatch1, sampleMatch2)

        val sampleStandingEntity1 = StandingEntity(
            teamName = "Man United",
            position = 1,
            played = 1,
            wins = 1,
            draws = 0,
            losses = 0,
            goalsFor = 1,
            goalsAgainst = 0,
            points = 3
        )

        val sampleStandingEntity2 = StandingEntity(
            teamName = "Arsenal",
            position = 2,
            played = 1,
            wins = 1,
            draws = 0,
            losses = 0,
            goalsFor = 2,
            goalsAgainst = 1,
            points = 3
        )

        val standingEntitiesList = listOf(sampleStandingEntity1, sampleStandingEntity2)

        val sampleStanding1 = Standing(
            position = 1,
            teamName = "Man United",
            played = 1,
            wins = 1,
            draws = 0,
            losses = 0,
            goalsFor = 1,
            goalsAgainst = 0,
            points = 3
        )

        val sampleStanding2 = Standing(
            position = 2,
            teamName = "Arsenal",
            played = 1,
            wins = 1,
            draws = 0,
            losses = 0,
            goalsFor = 2,
            goalsAgainst = 1,
            points = 3
        )

        val domainStandingsList = listOf(sampleStanding1, sampleStanding2)
    }
    @Before
    fun setUp() {
        matchDao = mockk()
        standingDao = mockk()
        favoriteTeamDao = mockk()
        matchRepository = MatchRepositoryImpl(matchDao, standingDao, favoriteTeamDao)
    }

    @After
    fun tearDown() {
        unmockkObject(RetrofitClient)
    }

/*    @Test
    fun `getMatches should return empty list if no matches are found`() = runTest {
        every { matchDao.getMatches() } returns flowOf(emptyList<MatchEntity>())

        val actualList = matchRepository.getMatches().first()

        assertEquals(emptyList<Match>(), actualList)
    }

    @Test
    fun `getMatches should return matches if matches are found`() = runTest {
        val matches = listOf(
            MatchEntity(1, 1, "2026-01-01T15:00:00Z", "Location", "HomeTeam", "AwayTeam", null, 1, 0, "HomeTeam"),
            MatchEntity(2, 1, "2026-01-02T15:00:00Z", "Location", "HomeTeam", "AwayTeam", null, 2, 1, "AwayTeam"),
            MatchEntity(3, 1, "2026-01-03T15:00:00Z", "Location", "HomeTeam", "AwayTeam", null, 3, 2, "HomeTeam"),
        )
        every { matchDao.getMatches() } returns flowOf(matches)

        val actualMatches = matchRepository.getMatches().first()

        assertEquals(matches.map { it.matchNumber }, actualMatches.map { it.matchNumber })
        assertEquals(matches.map { it.homeTeam }, actualMatches.map { it.homeTeam })
        assertEquals(matches.map { it.awayTeam }, actualMatches.map { it.awayTeam })
    }*/

    @Test
    fun `getMatches should propagate exception if dao throws`() = runTest {
        every { matchDao.getMatches() } throws RuntimeException("Test exception")

        var thrown: Throwable? = null
        try {
            matchRepository.getMatches().first()
        } catch (e: Throwable) {
            thrown = e
        }

        assertEquals("Test exception", thrown?.message)
    }

    @Test
    fun `getStandings should return empty list if no standings are found`() = runTest {
        every { standingDao.getStandings() } returns flowOf(emptyList<StandingEntity>())
        val actualList = matchRepository.getStandings().first()
        val expectedList = emptyList<Standing>()

        assertEquals(expectedList, actualList)
    }

    @Test
    fun `getStandings should return standings if standings are found`() = runTest {
        val dbEntities = listOf(
            StandingEntity(
                teamName = "Team1",
                position = 1,
                played = 10,
                wins = 5,
                draws = 3,
                losses = 2,
                goalsFor = 15,
                goalsAgainst = 10,
                points = 18
            ),
            StandingEntity(
                teamName = "Team2",
                position = 2,
                played = 10,
                wins = 4,
                draws = 4,
                losses = 2,
                goalsFor = 12,
                goalsAgainst = 9,
                points = 16
            )
        )

        every { standingDao.getStandings() } returns flowOf(dbEntities)

        val actualList = matchRepository.getStandings().first()

        val expectedDomainList = listOf(
            Standing(
                position = 1,
                teamName = "Team1",
                played = 10,
                wins = 5,
                draws = 3,
                losses = 2,
                goalsFor = 15,
                goalsAgainst = 10,
                points = 18
            ),
            Standing(
                position = 2,
                teamName = "Team2",
                played = 10,
                wins = 4,
                draws = 4,
                losses = 2,
                goalsFor = 12,
                goalsAgainst = 9,
                points = 16
            )
        )

        assertEquals(expectedDomainList, actualList)
    }

    @Test
    fun `getMatch should return match if found`() = runTest {
        val matchId = 1
        val dbEntity = TestData.sampleMatchEntity1
        val expectedDomainMatch = TestData.sampleMatch1

        every { matchDao.getMatch(matchId) } returns flowOf(dbEntity)
        every { matchDao.getMatches() } returns flowOf(TestData.matchEntitiesList)
        val actualMatch = matchRepository.getMatch(matchId).first()
        assertEquals(expectedDomainMatch, actualMatch)
    }

/*    @Test
    fun `getMatch should return empty list if not found` () = runTest {
        val matchId = 1
        val expectedList = emptyList<Match>()
        every { matchDao.getMatches() } returns flowOf(emptyList())
        every {matchDao.getMatch(matchId)} returns flowOf(emptyList())
    }*/
}
