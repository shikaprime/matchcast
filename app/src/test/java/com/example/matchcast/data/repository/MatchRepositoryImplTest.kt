package com.example.matchcast.data.repository
import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.local.MatchEntity
import com.example.matchcast.data.local.StandingDao
import com.example.matchcast.data.local.StandingEntity
import com.example.matchcast.data.remote.RetrofitClient
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


class MatchRepositoryImplTest{
    private lateinit var matchRepository: MatchRepository
    private lateinit var matchDao: MatchDao
    private lateinit var standingDao: StandingDao

    @Before
    fun setUp(){
        matchDao = mockk()
        standingDao = mockk()
        matchRepository = MatchRepositoryImpl( matchDao, standingDao)
    } 
    @After
    fun tearDown(){
        unmockkObject(RetrofitClient)
    }

    @Test
    fun `getMatches should return empty list if no matches are found`() = runTest {
        every { matchDao.getMatches() } returns flowOf(emptyList<MatchEntity>())

        val actualFlow =flowOf( matchRepository.getMatches().first())
        val emptyEntity = emptyList<MatchEntity>()
        assertEquals( emptyEntity, actualFlow)
    }

    @Test
    fun `getMatches should return matches if matches are found`() = runTest {
        val matches = listOf(
            MatchEntity(1, 1, "2026-01-01", "Location", "HomeTeam", "AwayTeam", null,1, 0, "HomeTeam"),
            MatchEntity(2, 1, "2026-01-02", "Location", "HomeTeam", "AwayTeam", null,2, 1, "AwayTeam"),
            MatchEntity(3, 1, "2026-01-03", "Location", "HomeTeam", "AwayTeam", null,3, 2, "HomeTeam"),
        )
        every { matchDao.getMatches() } returns flowOf(matches)

        val actualFlow =flowOf( matchRepository.getMatches().first())
        assertEquals( matches, actualFlow)
    }

    @Test
    fun `getMatches should return error if getMatches throws an exception`() = runTest {
        every { matchDao.getMatches() } throws Exception("Test exception")

        val actualFlow =flowOf( matchRepository.getMatches().first())
        assertEquals( Exception("Test exception"), actualFlow)
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
}

