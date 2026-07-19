package com.example.matchcast.data.repository

import com.example.matchcast.data.local.FavoriteTeamDao
import com.example.matchcast.data.local.FavoriteTeamEntity
import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.local.StandingDao
import com.example.matchcast.data.local.StandingEntity
import com.example.matchcast.data.remote.MatchDto
import com.example.matchcast.data.remote.RetrofitClient
import com.example.matchcast.domain.model.FavoriteTeam
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.domain.model.Standing
import com.example.matchcast.domain.repository.MatchRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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

    const val NET_DATE_1 = "2026-10-24 15:00:00Z"
    const val NET_DATE_2 = "2026-10-25 17:30:00Z"

    const val EXPECTED_FORMATTED_DATE_1 = "24.10.2026 18:00"
    const val EXPECTED_FORMATTED_DATE_2 = "25.10.2026 20:30"



    val networkMatchesList = listOf(
        MatchDto(1, 1, NET_DATE_1, "Old Trafford", "Man United", "Wolves", "Group A", 2, 0, "Man United"),
        MatchDto(2, 1, NET_DATE_2, "Emirates Stadium", "Arsenal", "Nottingham Forest", "Group A", 1, 1, "Draw")
    )


    val sampleMatchEntity1 = MatchEntity(
        matchNumber = 1,
        roundNumber = 1,
        dateUtc = NET_DATE_1,
        location = "Old Trafford",
        homeTeam = "Man United",
        awayTeam = "Wolves",
        group = "Group A",
        homeTeamScore = 2,
        awayTeamScore = 0,
        winner = "Man United"
    )

    val sampleMatchEntity2 = MatchEntity(
        matchNumber = 2,
        roundNumber = 1,
        dateUtc = NET_DATE_2,
        location = "Emirates Stadium",
        homeTeam = "Arsenal",
        awayTeam = "Nottingham Forest",
        group = "Group A",
        homeTeamScore = 1,
        awayTeamScore = 1,
        winner = "Draw"
    )

    val matchEntitiesList = listOf(sampleMatchEntity1, sampleMatchEntity2)


    val sampleMatch1 = Match(
        matchNumber = 1,
        roundNumber = 1,
        formattedDateUtc = EXPECTED_FORMATTED_DATE_1,
        location = "Old Trafford",
        homeTeam = "Man United",
        awayTeam = "Wolves",
        group = "Group A",
        homeTeamScore = 2,
        awayTeamScore = 0,
        winner = "Man United",
        homeTeamForm = ArrayDeque(listOf(MatchOutcome.WIN)),
        awayTeamForm = ArrayDeque(listOf(MatchOutcome.LOSE))
    )

    val sampleMatch2 = Match(
        matchNumber = 2,
        roundNumber = 1,
        formattedDateUtc = EXPECTED_FORMATTED_DATE_2,
        location = "Emirates Stadium",
        homeTeam = "Arsenal",
        awayTeam = "Nottingham Forest",
        group = "Group A",
        homeTeamScore = 1,
        awayTeamScore = 1,
        winner = "Draw",
        homeTeamForm = ArrayDeque(listOf(MatchOutcome.DRAW)),
        awayTeamForm = ArrayDeque(listOf(MatchOutcome.DRAW))
    )

    val domainMatchesList = listOf(sampleMatch1, sampleMatch2)


    val sampleStandingEntity = StandingEntity(
        position = 1,
        teamName = "Man United",
        played = 1,
        wins = 1,
        draws = 0,
        losses = 0,
        goalsFor = 2,
        goalsAgainst = 0,
        points = 3
    )

    val sampleStanding = Standing(
        position = 1,
        teamName = "Man United",
        played = 1,
        wins = 1,
        draws = 0,
        losses = 0,
        goalsFor = 2,
        goalsAgainst = 0,
        points = 3
    )


    val sampleFavoriteTeamEntity = FavoriteTeamEntity(
        teamName = "Man United",
        addedAt = 1718294829123L
    )

    val sampleFavoriteTeam = FavoriteTeam(
        teamName = "Man United",
        fullName = "Man United" 
    )
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

    @Test
    fun `getMatch should throw if match is not found`() = runTest {
        val matchId = 999

        @Suppress("UNCHECKED_CAST")
        every { matchDao.getMatch(matchId) } returns (flowOf(null) as Flow<MatchEntity>)
        every { matchDao.getMatches() } returns flowOf(emptyList())

        var thrown: Throwable? = null
        try {
            matchRepository.getMatch(matchId).first()
        } catch (e: Throwable) {
            thrown = e
        }

        assertEquals(Exception::class.java, thrown?.javaClass)
    }

    @Test
    fun `getFavoriteTeams should return favorite teams if found`() = runTest {
        every { favoriteTeamDao.getFavorites() } returns flowOf(listOf(TestData.sampleFavoriteTeamEntity))
        val expectedList = listOf(TestData.sampleFavoriteTeam)
        val actualList = matchRepository.getFavoriteTeams().first()
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `getFavoriteTeams should return empty list if no favorite teams are found`() = runTest {
        every { favoriteTeamDao.getFavorites() } returns flowOf(emptyList())
        val expectedList = emptyList<FavoriteTeam>()
        val actualList = matchRepository.getFavoriteTeams().first()
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `isFavoriteTeam should return true if team is favorite`() = runTest {
        every { favoriteTeamDao.isFavorite(TestData.sampleFavoriteTeam.teamName) } returns flowOf(true)
        val actual = matchRepository.isFavoriteTeam(TestData.sampleFavoriteTeam.teamName).first()
        assertEquals(true, actual)
    }

    @Test
    fun `isFavoriteTeam should return false if team is not favorite`() = runTest {
        every { favoriteTeamDao.isFavorite(TestData.sampleFavoriteTeam.teamName) } returns flowOf(false)
        val actual = matchRepository.isFavoriteTeam(TestData.sampleFavoriteTeam.teamName).first()
        assertEquals(false, actual)
    }
    
    @Test 
    fun `getMatches should return empty List if no matches are found`() = runTest {
        every{matchDao.getMatches()} returns flowOf(emptyList())
        val actual = matchRepository.getMatches().first()
        val expected = emptyList<Match>()
        assertEquals(expected, actual)
    }

    @Test
    fun `getMatches should return matches if matches are found`() = runTest {
        every{matchDao.getMatches()} returns flowOf(TestData.matchEntitiesList)
        val actual = matchRepository.getMatches().first()
        val expected = TestData.domainMatchesList
        assertEquals(expected, actual)
    }
}
