package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchcast.presentaion.theme.DividerGray
import com.example.matchcast.presentaion.theme.MatchCastTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateComp(
    date: String,
    count: Int
){
    val dateParts = date.split(" ")
    val formattedDate = formSimpleDate(dateParts.first())
    var match: String = ""
    match = when(count){
        1 -> "матч"
        2,3,4 -> "матча"
        else -> "матчей"
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){

        Text(
            text = formattedDate.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(Modifier.weight(1f), thickness = 2.dp, color = DividerGray.copy(1.1f))
        Text(
            text = "$count $match",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UseDateComp(){
    MatchCastTheme {
        DateComp("11.08.2023 19:00",3)
    }
}




fun formSimpleDate(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        parsedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("ru")))
    } catch (e: Exception) {
        dateString
    }
}