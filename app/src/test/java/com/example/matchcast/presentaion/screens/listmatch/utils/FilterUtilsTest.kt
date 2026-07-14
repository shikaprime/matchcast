package com.example.matchcast.presentaion.screens.listmatch.utils

import com.example.matchcast.domain.model.Match
import com.example.matchcast.presentaion.screens.listmatch.states.FilterType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterUtilsTest {

    private fun match(
        matchNumber: Int,
        roundNumber: Int,
        formattedDateUtc: String,
        location: String,
        homeTeam: String,
        awayTeam: String
    ) = Match(
        matchNumber = matchNumber,
        roundNumber = roundNumber,
        formattedDateUtc = formattedDateUtc,
        location = location,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        group = null,
        homeTeamScore = 1,
        awayTeamScore = 0,
        winner = homeTeam,
        homeTeamForm = null,
        awayTeamForm = null
    )

    private val arsenalVsChelsea = match(
        matchNumber = 1,
        roundNumber = 1,
        formattedDateUtc = "11.08.2023 19:00",
        location = "Emirates Stadium",
        homeTeam = "Arsenal",
        awayTeam = "Chelsea"
    )

    private val liverpoolVsArsenal = match(
        matchNumber = 2,
        roundNumber = 1,
        formattedDateUtc = "12.08.2023 15:00",
        location = "Anfield",
        homeTeam = "Liverpool",
        awayTeam = "Arsenal"
    )

    private val chelseaVsLiverpool = match(
        matchNumber = 3,
        roundNumber = 2,
        formattedDateUtc = "02.09.2023 16:30",
        location = "Stamford Bridge",
        homeTeam = "Chelsea",
        awayTeam = "Liverpool"
    )

    private val allMatches = listOf(arsenalVsChelsea, liverpoolVsArsenal, chelseaVsLiverpool)


    @Test
    fun `extractAvailableFilters returns empty map for empty match list`() {
        val result = FilterUtils.extractAvailableFilters(emptyList())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `extractAvailableFilters returns distinct sorted rounds`() {
        val result = FilterUtils.extractAvailableFilters(allMatches)

        assertEquals(listOf("1", "2"), result[FilterType.ROUND])
    }

    @Test
    fun `extractAvailableFilters combines and dedupes home and away teams`() {
        val result = FilterUtils.extractAvailableFilters(allMatches)

        assertEquals(listOf("Arsenal", "Chelsea", "Liverpool"), result[FilterType.TEAM])
    }

    @Test
    fun `extractAvailableFilters returns distinct sorted stadiums`() {
        val result = FilterUtils.extractAvailableFilters(allMatches)

        assertEquals(listOf("Anfield", "Emirates Stadium", "Stamford Bridge"), result[FilterType.STADIUM])
    }

    @Test
    fun `extractAvailableFilters returns months sorted chronologically`() {
        val result = FilterUtils.extractAvailableFilters(allMatches)

        assertEquals(listOf("08.2023", "09.2023"), result[FilterType.MONTH])
    }

    @Test
    fun `extractAvailableFilters ignores unparsable dates for month filter`() {
        val matchWithBadDate = match(
            matchNumber = 4,
            roundNumber = 1,
            formattedDateUtc = "not-a-date",
            location = "Some Stadium",
            homeTeam = "Everton",
            awayTeam = "Fulham"
        )

        val result = FilterUtils.extractAvailableFilters(listOf(matchWithBadDate))

        assertTrue(result[FilterType.MONTH]?.isEmpty() == true)
    }


    @Test
    fun `applyFilters with no filters and no query returns all matches`() {
        val result = FilterUtils.applyFilters(allMatches, emptyMap(), "")

        assertEquals(allMatches, result)
    }

    @Test
    fun `applyFilters by team matches both home and away appearances`() {
        val result = FilterUtils.applyFilters(
            allMatches,
            mapOf(FilterType.TEAM to "Arsenal"),
            ""
        )

        assertEquals(setOf(arsenalVsChelsea, liverpoolVsArsenal), result.toSet())
    }

    @Test
    fun `applyFilters by round only returns matches from that round`() {
        val result = FilterUtils.applyFilters(
            allMatches,
            mapOf(FilterType.ROUND to "2"),
            ""
        )

        assertEquals(listOf(chelseaVsLiverpool), result)
    }

    @Test
    fun `applyFilters combines multiple filters with AND semantics`() {
        val result = FilterUtils.applyFilters(
            allMatches,
            mapOf(FilterType.TEAM to "Chelsea", FilterType.ROUND to "1"),
            ""
        )

        assertEquals(listOf(arsenalVsChelsea), result)
    }

    @Test
    fun `applyFilters search query matches case-insensitively`() {
        val result = FilterUtils.applyFilters(allMatches, emptyMap(), "chelsea")

        assertEquals(setOf(arsenalVsChelsea, chelseaVsLiverpool), result.toSet())
    }

    @Test
    fun `applyFilters combines active filters and search query together`() {
        val result = FilterUtils.applyFilters(
            allMatches,
            mapOf(FilterType.ROUND to "1"),
            "liverpool"
        )

        assertEquals(listOf(liverpoolVsArsenal), result)
    }

    @Test
    fun `applyFilters returns empty list when nothing matches`() {
        val result = FilterUtils.applyFilters(
            allMatches,
            mapOf(FilterType.STADIUM to "Old Trafford"),
            ""
        )

        assertTrue(result.isEmpty())
    }


    @Test
    fun `displayLabel prefixes round values`() {
        assertEquals("Тур 3", FilterUtils.displayLabel(FilterType.ROUND, "3"))
    }

    @Test
    fun `displayLabel passes team and stadium values through unchanged`() {
        assertEquals("Arsenal", FilterUtils.displayLabel(FilterType.TEAM, "Arsenal"))
        assertEquals("Anfield", FilterUtils.displayLabel(FilterType.STADIUM, "Anfield"))
    }

    @Test
    fun `displayLabel formats month key as localized month and year`() {
        assertEquals("Август 2023", FilterUtils.displayLabel(FilterType.MONTH, "08.2023"))
    }
}
