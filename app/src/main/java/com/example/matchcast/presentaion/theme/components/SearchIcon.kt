package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchcast.presentaion.theme.EplPurple
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.microsoft.fluent.mobile.icons.R


@Composable
fun SearchIcon(
    onClick : () -> Unit = {},
    modifier: Modifier
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                shape = CircleShape,
                color = EplPurple.copy(0.3f)
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_fluent_search_24_regular ),
            contentDescription = "Search",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UseSearchIcon(){
    MatchCastTheme {
        SearchIcon(
            modifier = Modifier
        )
    }
}