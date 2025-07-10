package com.example.stock.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> AutoScrollList(
    items: List<T>,
    modifier: Modifier = Modifier,
    autoScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    stickyHeaderContent: @Composable () -> Unit,
    itemContent: @Composable LazyItemScope.(item: T, index: Int) -> Unit,

    ) {
    val listState = rememberLazyListState()
    val previousItemCount = remember { mutableIntStateOf(items.size) }

    LaunchedEffect(items.size) {
        if (autoScrollEnabled && items.size > previousItemCount.intValue && items.isNotEmpty()) {
            listState.animateScrollToItem(items.size - 1)
        }
        previousItemCount.intValue = items.size
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        reverseLayout = reverseLayout,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        stickyHeader {
            stickyHeaderContent()
        }

        itemsIndexed(
            items = items,
            key = { index, item ->
                when (item) {
                    is Any -> item.hashCode() + index
                    else -> index
                }
            }
        ) { index, item ->
            itemContent(item, index)
        }
    }
}
