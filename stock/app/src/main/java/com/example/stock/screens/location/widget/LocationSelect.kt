package com.example.stock.screens.location.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.stock.domain.entities.LocationEntity
import com.example.stock.entities.LocationKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelect(
    location : LocationEntity,
    onLocationChange : (Long) -> Unit,
) {
    val viewModel: LocationSelectViewModel = viewModel(
        factory = locationSelectViewModelFactory(location.id)
    )
    val state by viewModel.state.collectAsState()
    var isShowLocationSelect by remember { mutableStateOf(false) }

    LaunchedEffect(location.id) {
        viewModel.refresh()
    }

    Row {
        if(state.currentLocationKey != null) {
            LocationKeyDropdown(
                locationKey = state.currentLocationKey!!,
                onClick = { isShowLocationSelect = true }
            )
        }

    }
    LocationSelectBottomSheet(
        locationKeys = state.locationKeys,
        selectedLocation = state.currentLocationKey,
        isVisible =  isShowLocationSelect,
        onDismiss = { isShowLocationSelect = false },
        onLocationSelected = { location ->
            onLocationChange(location.id)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationKeyDropdown(
    locationKey : LocationKey,
    onClick : () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "${locationKey.division}. ${locationKey.name}",
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onClick() },
            fontSize = 15.sp,
            maxLines = 1
            // fontSize = 16.sp
        )
    }
}
