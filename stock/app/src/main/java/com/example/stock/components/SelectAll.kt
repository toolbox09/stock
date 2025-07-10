package com.example.stock.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

@Composable
fun <T> SelectAll(
    items: List<T>,
    selectedIds: Set<Long>,
    onSelectionChange: (Set<Long>) -> Unit,
    getItemId: (T) -> Long,
    modifier: Modifier = Modifier,
    headerContent: @Composable RowScope.() -> Unit
) {
    val allSelected = items.isNotEmpty() && selectedIds.size == items.size
    val indeterminate = selectedIds.isNotEmpty() && !allSelected

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TriStateCheckbox(
            state = when {
                allSelected -> ToggleableState.On
                indeterminate -> ToggleableState.Indeterminate
                else -> ToggleableState.Off
            },
            onClick = {
                onSelectionChange(
                    if (allSelected) emptySet()
                    else items.map(getItemId).toSet()
                )
            }
        )
        Spacer(Modifier.width(8.dp))
        headerContent()
    }
}