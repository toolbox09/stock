package com.example.stock.screens.location

import com.example.stock.playSystemWarningSound
import android.app.Application
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stock.components.AutoScrollList
import com.example.stock.components.HeaderRow
import com.example.stock.components.SelectAll
import com.example.stock.domain.entities.ScanItemEntity
import com.example.stock.entities.mergeScanItem
import com.example.stock.screens.bluetooth.BluetoothViewModel
import com.example.stock.screens.location.widget.EditScanItemBottomSheet
import com.example.stock.screens.location.widget.LocationBottomAppBar
import com.example.stock.screens.location.widget.LocationSelect
import com.example.stock.screens.location.widget.MergeModeSwitch
import com.example.stock.screens.location.widget.NewScanItemBottomSheet
import com.example.stock.screens.location.widget.ScanItemCount
import com.example.stock.screens.location.widget.ScanItemItem
import com.example.stock.screens.main.MainNavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    locationId: Long,
    navController : NavHostController,
    onMenuClick: () -> Unit = {}
) {
    val viewModel: LocationViewModel = viewModel(
        factory = locationViewModelFactory(locationId)
    )
    val state by viewModel.state.collectAsState()
    var checkedIds by remember { mutableStateOf(setOf<Long>())}
    val scope = rememberCoroutineScope()
    val application = LocalContext.current.applicationContext as Application
    val bleViewModel: BluetoothViewModel = viewModel(factory = BluetoothViewModel.Factory(application))
    // val scannedData by bleViewModel.scannedData.collectAsStateWithLifecycle()

    var isShowEdite by remember { mutableStateOf(false) }
    var isShowNew by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ScanItemEntity?>(null) }
    var items by remember { mutableStateOf(listOf<ScanItemEntity>()) }

    var isMerge = remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(state.scanItems, isMerge.value) {
        items = if(isMerge.value) {
            mergeScanItem(state.scanItems)
        }else {
            state.scanItems
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()

        bleViewModel.scannedDataFlow.collectLatest { scannedData ->
            val targetCode = scannedData
            val (isValid, masterValue) = viewModel.validateBarcodeAndGetValue(targetCode)

            if (!isValid) {
                withContext(Dispatchers.Main) {
                    playSystemWarningSound(context = context)
                }
            }

            viewModel.insertScanItem(
                ScanItemEntity(
                    locationId = locationId,
                    barcode = targetCode,
                    count = 1,
                    isMatch = isValid,
                    isUpload = false,
                    masterName = masterValue ?: ""
                )
            )
        }
    }

    LaunchedEffect( state.createLocationId ) {
        state.createLocationId?.let { targetId ->
            navController.navigate("location/${targetId}")
            viewModel.clearCreateLocationId()
        }
    }

    fun addBarcode( barcode : String, count : Int ) {
        scope.launch {
            // 바코드 유효성 검사 (동기적 처리)
            val validate = viewModel.validateBarcodeAndGetValue(barcode)

            if (!validate.first) {
                withContext(Dispatchers.Main) {
                    playSystemWarningSound(context = context)
                }
            }

            viewModel.insertScanItem(
                ScanItemEntity(
                    locationId = locationId,
                    barcode = barcode,
                    count = count,
                    isMatch = validate.first,
                    isUpload = false,
                    masterName = validate.second ?: ""
                )
            )
        }
    }

    BackHandler {
        onMenuClick()
    }

    /*
    fun getRandomString(): String {
        val strings = listOf(
            "IB33KC772RD145",
            "IB33TR726PK165",
            "IB45BG566BKF",
            "1234567890",
            "0987654321"
        )

        return strings.random()
    }

    fun onTestAdd() : Unit {
        viewModel.insertScanItem(
            ScanItemEntity(
                locationId = locationId,
                barcode = getRandomString(),
                count = 1,
                isMatch = true,
                isUpload = false,
                masterName = "",
            )
        )
    }
    */

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    state.location?.let { location ->
                        LocationSelect (
                            location = location,
                            onLocationChange = { id ->
                                navController.navigate("location/${id}")
                            },
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(MainNavRoute.BluetoothModal.route)  }) {
                        Icon(Icons.Outlined.Bluetooth, contentDescription = "설정")
                    }
                }
            )
        },
        bottomBar = {
            LocationBottomAppBar(
                previousLocationId = state.previousLocationId,
                checkedIds = checkedIds,
                onBack = {  state.previousLocationId?.let { targetId -> navController.navigate("location/${targetId}") }  },
                onForward = {
                    if(state.nextLocationId != null) {
                        navController.navigate("location/${state.nextLocationId}")
                    }else {
                        state.location?.division?.let { division->
                            viewModel.createNextLocation(division)
                        }

                    }
                            },
                onAdd = {
                    isShowNew = true
                },
                onDelete = { viewModel.deleteScanItemByIds(checkedIds.toList()) },
                onDeleteAll = { viewModel.deleteScanItemByLocationId(locationId) }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                AutoScrollList(
                    items = items,
                    modifier = Modifier.padding(paddingValues),
                    stickyHeaderContent = {
                        HeaderRow {

                            if(isMerge.value == false) {
                                SelectAll(
                                    items = state.scanItems,
                                    selectedIds = checkedIds,
                                    onSelectionChange = { checkedIds = it },
                                    getItemId = { it.id },
                                    headerContent = {
                                        ScanItemCount(state.scanItems)
                                    }
                                )
                            }else {
                                Box{}
                            }

                            MergeModeSwitch( onCheckedChange = { merge -> isMerge.value = merge } )
                        }
                    }
                ){ item, index ->
                    ScanItemItem(
                        index = index,
                        isMerge = isMerge.value,
                        checked = checkedIds.contains(item.id),
                        scanItem = item,
                        onEdit = {
                            editingItem = item
                            isShowEdite = true
                        },
                        onDelete = {
                            viewModel.deleteScanItemById(item.id)
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




    NewScanItemBottomSheet(
        show = isShowNew,
        onAddItem = { barcode, count ->
            addBarcode( barcode, count)
        },
        onDismiss = { isShowNew = false }
    )

    editingItem?.let { item ->
        EditScanItemBottomSheet(
            show = isShowEdite,
            scanItemId = item.id,
            initialBarcode = item.barcode,
            initialCount = item.count.toString(),
            onSave = { scanItemId, barcode, count ->
                viewModel.updateScanItem(scanItemId, barcode, count)
            },
            onDismiss = {
                isShowEdite = false
                editingItem = null
            }
        )
    }
}
