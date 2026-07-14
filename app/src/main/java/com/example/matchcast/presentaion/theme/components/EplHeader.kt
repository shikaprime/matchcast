package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchcast.R
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun EplHeader(
    modifier: Modifier = Modifier,
    searchSlot: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.premier_league),
                contentDescription = "Премьер лига",
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        searchSlot()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun UseEplHeader() {
    MatchCastTheme {
        EplHeader(searchSlot = { SearchButton(onClick = {}) })
    }
}
