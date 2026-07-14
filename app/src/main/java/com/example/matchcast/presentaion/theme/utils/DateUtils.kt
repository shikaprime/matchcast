package com.example.matchcast.presentaion.theme.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val RU_LOCALE = Locale("ru")

private fun parseDisplayDate(dateString: String): LocalDate? = try {
    LocalDate.parse(dateString, DISPLAY_DATE_FORMAT)
} catch (_: Exception) {
    null
}

fun formatFullDate(dateString: String): String {
    return parseDisplayDate(dateString)?.format(
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", RU_LOCALE)
    ) ?: dateString
}

fun formatShortDate(dateString: String): String {
    return parseDisplayDate(dateString)?.format(
        DateTimeFormatter.ofPattern("dd MMM", RU_LOCALE)
    ) ?: dateString
}
