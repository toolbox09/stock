package com.example.stock.screens.work.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.stock.components.ItemMenuButton
import com.example.stock.components.JsConfirm
import com.example.stock.components.LocationName
import com.example.stock.entities.LocationInfo
import com.example.stock.toDateString

@Composable
fun LocationInfoItem(
    locationInfo: LocationInfo,
    checked: Boolean,
    onEdit: (LocationInfo) -> Unit,
    onDelete: (LocationInfo) -> Unit,
    onClick: (LocationInfo) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var deleteShowAlert by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(locationInfo) },
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        headlineContent = {
            LocationName(locationInfo.division, locationInfo.name)
        },
        supportingContent = {
            Text(locationInfo.createdTime.toDateString())
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Text("${locationInfo.count}개")
                ItemMenuButton(
                    expanded = expanded,
                    onExpandedChange = { value -> expanded = !expanded },
                    onDelete = { deleteShowAlert = true },
                    onEdite = { onEdit(locationInfo) }
                )
            }
        }
    )

    JsConfirm(
        show = deleteShowAlert,
        message = "정말 삭제 하시겠습니까?",
        onConfirm = {
            onDelete(locationInfo)
            deleteShowAlert = false
        },
        onCancel = {
            deleteShowAlert = false
        }
    )
}