package com.example.stock.screens.work

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stock.components.*
import com.example.stock.entities.LocationInfo
import com.example.stock.screens.main.MainNavRoute
import com.example.stock.screens.work.widget.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkScreen(
    navController : NavHostController,
    onMenuClick: () -> Unit,
) {
    val viewModel: WorkViewModel = viewModel(factory = workViewModelFactory())
    val state by viewModel.state.collectAsState()

    var division by remember { mutableStateOf("가") }
    var checkedIds by remember { mutableStateOf(setOf<Long>()) }

    var isShowSelectProject by remember { mutableStateOf(false) }

    var targetLocationInfo by remember { mutableStateOf<LocationInfo?>(null) }
    var isShowEditLocation by remember { mutableStateOf(false) }

    var isUploadAll by remember { mutableStateOf(false) }
    var isShowUpload by remember { mutableStateOf(false) }

    LaunchedEffect(isShowUpload) {
        if(isShowUpload == false) {
            isUploadAll = false
        }
    }


    LaunchedEffect( state.createLocationId ) {
        if(state.createLocationId != null) {
            navController.navigate("location/${state.createLocationId!!}")
            viewModel.clearCreateLocationId()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text( state.workInfo?.projectName ?: "재고조사", fontSize = 15.sp, maxLines = 1 ) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                },
                actions = {
                    state.workInfo?.let {
                        WorkTopBarActions(
                            onSearch = { navController.navigate(MainNavRoute.ScanItemSearch.route) },
                            onStartWork = { isShowSelectProject = true },
                            onCloseWork = { viewModel.close() }
                        )
                    }
                }
            )
        },
        bottomBar = {
            state.workInfo?.let { work ->

                AnimatedVisibility(
                    visible = checkedIds.isEmpty(),
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    WorkBottomBar(
                        onAddLocation = { viewModel.createNextLocation(division,  work.userKeyword ) },
                        onDeleteAll = { viewModel.deleteLocationAll()  },
                        onUploadAll = {
                            isUploadAll = true
                            isShowUpload = true
                        },
                    )
                }

                AnimatedVisibility(
                    visible = checkedIds.isEmpty() == false,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    WorkCheckedBottomBar(
                        onAddLocation = { viewModel.createNextLocation(division,  work.userKeyword ) },
                        onDeleteAll = { viewModel.deleteLocationAll()  },
                        onUploadAll = {
                            isUploadAll = true
                            isShowUpload = true
                        },
                        onDelete = { viewModel.deleteLocationByIds(checkedIds.toList()) },
                        onUpload = {
                            isUploadAll = false
                            isShowUpload = true
                        }
                    )
                }

            }
        }
    ){ padding ->
        when {
            state.isLoading -> {
                LoadingPanel()
            }

            state.workInfo == null -> {
                EmptyWorkPanel(
                    modifier = Modifier.padding(padding),
                    onStartWork = { isShowSelectProject = true }
                )
            }
            else -> {
                AutoScrollList(
                    items = state.locationInfos,
                    modifier = Modifier.padding(padding),
                    stickyHeaderContent = {
                        HeaderRow {
                            SelectAll(
                                items = state.locationInfos,
                                selectedIds = checkedIds,
                                onSelectionChange = { checkedIds = it },
                                getItemId = { it.id },
                                headerContent = {
                                    TotalScanItemCount(state.locationInfos)
                                }
                            )
                            DivisionSelector(onValueChange = { value -> division = value })
                        }
                    }
                ){ item, index ->
                    LocationInfoItem(
                        locationInfo = item,
                        checked = checkedIds.contains(item.id),
                        onEdit = { location ->
                            targetLocationInfo = location
                            isShowEditLocation = true
                        },
                        onDelete = { location ->
                            viewModel.deleteLocationById(location.id)
                        },
                        onClick = {
                            navController.navigate("location/${item.id}")
                        },
                        onCheckedChange = { checked ->
                            checkedIds = if (checked)
                                checkedIds + item.id
                            else
                                checkedIds - item.id
                        }
                    )
                }
            }
        }
    }

    if (isShowSelectProject) {
        ProjectSelectModal(
            onProjectSelected = { project ->
                viewModel.open(project.name)
            },
            onDismissRequest = { isShowSelectProject = false }
        )
    }

    if(isShowUpload) {
        ScanItemUploadModal(
            locationIds = if(isUploadAll) emptyList<Long>() else checkedIds.toList(),
            isVisible = isShowUpload,
            onDismiss = { isShowUpload = false },
        )
    }

    targetLocationInfo?.let { target ->
        EditLocationBottomSheet(
            show = isShowEditLocation,
            locationId = target.id,
            initDivision = target.division,
            initName = target.name,
            onSave = { locationId, division, name ->
                viewModel.updateLocation(locationId, division, name)
            },
            onDismiss = {
                targetLocationInfo = null
                isShowEditLocation = false
            }
        )
    }

}