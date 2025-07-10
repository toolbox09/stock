package com.example.stock.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun ItemMenuButton(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    onDelete : () -> Unit = {},
    onEdite : () -> Unit = {},
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
                text = { Text("편집") },
                onClick = { onEdite()}
            )
            DropdownMenuItem(
                text = { Text("삭제") },
                onClick = { onDelete() }
            )
        }
    }
}