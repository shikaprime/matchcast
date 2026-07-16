package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.microsoft.fluent.mobile.icons.R

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(13.dp)
            )
            .height(32.dp)
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SearchIcon()
        Text(
            text = "Поиск матча",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun SearchInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val focusRequester = remember{ FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .focusRequester(focusRequester),
        placeholder = {
            Text(
                text = "Поиск матча...",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_fluent_search_24_regular),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Закрыть поиск",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 14.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.12f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.06f),

            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),

            cursorColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(13.dp)
    )
}




@Composable
@Preview
fun UseSearchButton(){
    MatchCastTheme {
        SearchButton()
    }
}

@Composable
fun SearchIcon(
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_fluent_search_24_regular),
            contentDescription = "Search",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
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