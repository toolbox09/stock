package com.example.stock.screens.work

import com.example.stock.domain.entities.*
import com.example.stock.components.*
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stock.components.SearchBar
import com.example.stock.entities.ScanItemForSearch
import kotlin.text.ifEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanItemSearchScreen(
    navController : NavHostController,
    onNavigateBack: () -> Unit
) {
    val viewModel : ScanItemSearchViewModel = viewModel(factory = scanItemSearchViewModelFactory())
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("재고 검색", fontSize = 15.sp, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    viewModel.search(searchQuery)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            SearchResultList( items = state.scanItems, onGo = { locationId ->  navController.navigate("location/${locationId}") } )
        }
    }
}

@Composable
private fun SearchResultList(
    items: List<ScanItemForSearch>,
    onGo : ( Long ) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            HeaderRow {
                ScanItemCount(items)
                Box {}
            }
        }
        items(items, key = { it.id }) { item ->
            SearchResultItem(scanItem = item, onGo = onGo )
        }
    }
}

@Composable
fun SearchResultItem(
    scanItem: ScanItemForSearch,
    onGo : ( Long ) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }



    ListItem(
        modifier = Modifier.fillMaxWidth(),
        overlineContent = {
            LocationName(scanItem.locationDivision, scanItem.locationName)
        },
        headlineContent = {
            Text(scanItem.barcode, color =  if(scanItem.isMatch) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error )
        },
        supportingContent = {
            Text(scanItem.masterName.ifEmpty { "-" })
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Text("${scanItem.count}개")
                SearchResultItemMenuButton(
                    expanded = expanded,
                    onExpandedChange = { value -> expanded = !expanded },
                    onGo = { onGo( scanItem.locationId ) },
                )
            }
        }
    )
}


@Composable
fun SearchResultItemMenuButton(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    onGo : () -> Unit = {},
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
                text = { Text("바로가기") },
                onClick = {
                    onGo()
                    internalExpanded = false
                }
            )
        }
    }
}

@Composable
fun ScanItemCount(scanItems: List<ScanItemForSearch> ) {
    val totalCount = remember(scanItems) {
        scanItems.sumOf { it.count }
    }

    Text("$totalCount 개")
}

