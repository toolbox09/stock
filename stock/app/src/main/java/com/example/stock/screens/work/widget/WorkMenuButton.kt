package com.example.stock.screens.work.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun WorkMenuButton(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    onNewWork : () -> Unit = {},
    onCloseWork : () -> Unit = {},
) {
    var internalExpanded by remember { mutableStateOf(false) }
    val finalExpanded = if (expanded != internalExpanded) expanded else internalExpanded

    Box(modifier = modifier) {
        IconButton(
            onClick = {
                internalExpanded = !internalExpanded
                onExpandedChange(internalExpanded)
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = finalExpanded,
            onDismissRequest = {
                internalExpanded = false
                onExpandedChange(false)
            }
        ) {
            DropdownMenuItem(
                text = { Text("새 작업") },
                onClick = { onNewWork()}
            )
            DropdownMenuItem(
                text = { Text("작업 종료") },
                onClick = { onCloseWork() }
            )
        }
    }
}