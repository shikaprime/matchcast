package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.presentaion.screens.listmatch.states.FilterType
import com.example.matchcast.presentaion.screens.listmatch.utils.FilterUtils
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun MatchFilters(
    availableFilters: Map<FilterType, List<String>>,
    activeFilters: Map<FilterType, String>,
    onFilterOptionSelected: (FilterType, String) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (availableFilters.values.all { it.isEmpty() }) return

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(FilterType.entries.toList(), key = { it.name }) { filterType ->
            val options = availableFilters[filterType].orEmpty()
            if (options.isNotEmpty()) {
                FilterPill(
                    filterType = filterType,
                    options = options,
                    selectedValue = activeFilters[filterType],
                    onOptionSelected = { value -> onFilterOptionSelected(filterType, value) }
                )
            }
        }

        if (activeFilters.isNotEmpty()) {
            item(key = "clear_all_filters") {
                ClearFiltersPill(onClick = onClearFilters)
            }
        }
    }
}

@Composable
private fun FilterPill(
    filterType: FilterType,
    options: List<String>,
    selectedValue: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val isActive = selectedValue != null

    val backgroundColor = if (isActive) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (isActive) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val borderColor = if (isActive) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(36.dp)
                .clip(CircleShape)
                .background(color = backgroundColor, shape = CircleShape)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .clickable { expanded = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isActive) FilterUtils.displayLabel(filterType, selectedValue) else filterType.label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 13.sp,
                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1
            )
            if (isActive) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Сбросить фильтр «${filterType.label}»",
                    tint = contentColor,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .clickable { onOptionSelected(selectedValue) }
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val isSelected = option == selectedValue
                DropdownMenuItem(
                    text = {
                        Text(
                            text = FilterUtils.displayLabel(filterType, option),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun ClearFiltersPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f), shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "Очистить",
            style = MaterialTheme.typography.labelMedium,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchFiltersInactivePreview() {
    MatchCastTheme {
        MatchFilters(
            availableFilters = mapOf(
                FilterType.ROUND to listOf("1", "2", "3"),
                FilterType.TEAM to listOf("Arsenal", "Chelsea", "Liverpool"),
                FilterType.STADIUM to listOf("Emirates Stadium", "Anfield"),
                FilterType.MONTH to listOf("08.2023", "09.2023")
            ),
            activeFilters = emptyMap(),
            onFilterOptionSelected = { _, _ -> },
            onClearFilters = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchFiltersActivePreview() {
    MatchCastTheme {
        MatchFilters(
            availableFilters = mapOf(
                FilterType.ROUND to listOf("1", "2", "3"),
                FilterType.TEAM to listOf("Arsenal", "Chelsea", "Liverpool"),
                FilterType.STADIUM to listOf("Emirates Stadium", "Anfield"),
                FilterType.MONTH to listOf("08.2023", "09.2023")
            ),
            activeFilters = mapOf(
                FilterType.ROUND to "1",
                FilterType.TEAM to "Arsenal"
            ),
            onFilterOptionSelected = { _, _ -> },
            onClearFilters = {}
        )
    }
}
