package com.example.matchcast.presentaion.screens.listmatch.utils

import com.example.matchcast.domain.model.Match
import com.example.matchcast.presentaion.screens.listmatch.states.FilterType
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale


object FilterUtils {

    private val ruLocale = Locale("ru")

    fun extractAvailableFilters(matches: List<Match>): Map<FilterType, List<String>> {
        if (matches.isEmpty()) return emptyMap()

        return mapOf(
            FilterType.ROUND to matches
                .map { it.roundNumber.toString() }
                .distinct()
                .sortedBy { it.toIntOrNull() ?: 0 },
            FilterType.TEAM to (matches.map { it.homeTeam } + matches.map { it.awayTeam })
                .distinct()
                .sorted(),
            FilterType.STADIUM to matches
                .map { it.location }
                .distinct()
                .sorted(),
            FilterType.MONTH to matches
                .mapNotNull { monthKey(it.formattedDateUtc) }
                .distinct()
                .sortedWith(compareBy(
                    { it.substringAfter(".").toIntOrNull() ?: 0 },
                    { it.substringBefore(".").toIntOrNull() ?: 0 }
                ))
        )
    }

    fun applyFilters(
        matches: List<Match>,
        activeFilters: Map<FilterType, String>,
        searchQuery: String
    ): List<Match> {
        return matches.filter { match ->
            matchesActiveFilters(match, activeFilters) && matchesSearchQuery(match, searchQuery)
        }
    }

    fun displayLabel(filterType: FilterType, rawValue: String): String {
        return when (filterType) {
            FilterType.ROUND -> "Тур $rawValue"
            FilterType.MONTH -> formatMonthLabel(rawValue)
            FilterType.TEAM, FilterType.STADIUM -> rawValue
        }
    }

    private fun matchesActiveFilters(match: Match, activeFilters: Map<FilterType, String>): Boolean {
        return activeFilters.all { (filterType, value) ->
            when (filterType) {
                FilterType.ROUND -> match.roundNumber.toString() == value
                FilterType.TEAM -> match.homeTeam == value || match.awayTeam == value
                FilterType.STADIUM -> match.location == value
                FilterType.MONTH -> monthKey(match.formattedDateUtc) == value
            }
        }
    }

    private fun matchesSearchQuery(match: Match, query: String): Boolean {
        if (query.isBlank()) return true
        return match.homeTeam.contains(query, ignoreCase = true) ||
            match.awayTeam.contains(query, ignoreCase = true)
    }

    private fun monthKey(formattedDateUtc: String): String? {
        val datePart = formattedDateUtc.split(" ").firstOrNull() ?: return null
        val segments = datePart.split(".")
        if (segments.size != 3) return null
        val month = segments[1].toIntOrNull()?.takeIf { it in 1..12 } ?: return null
        val year = segments[2].toIntOrNull() ?: return null
        return "%02d.%d".format(month, year)
    }

    private fun formatMonthLabel(monthKeyValue: String): String {
        val parts = monthKeyValue.split(".")
        if (parts.size != 2) return monthKeyValue
        val month = parts[0].toIntOrNull() ?: return monthKeyValue
        val year = parts[1]
        val monthName = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, ruLocale)
            .replaceFirstChar { it.uppercase() }
        return "$monthName $year"
    }
}
