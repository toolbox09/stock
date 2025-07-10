package com.example.stock.screens.location.widget


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stock.components.ItemMenuButton
import com.example.stock.components.JsConfirm
import com.example.stock.domain.entities.ScanItemEntity
import kotlin.text.ifEmpty


@Composable
fun ScanItemItem(
    index : Int,
    isMerge : Boolean,
    scanItem: ScanItemEntity,
    checked: Boolean,
    onEdit: (ScanItemEntity) -> Unit,
    onDelete: (ScanItemEntity) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var deleteShowAlert by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        leadingContent = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )  {
                if(isMerge == false) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = onCheckedChange
                    )
                }

                Spacer(Modifier.width(12.dp))
                Text((index + 1 ).toString())
            }
        },
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(scanItem.barcode, color = if(scanItem.isMatch) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error )
                Spacer(Modifier.width(10.dp))
                if(scanItem.isUpload) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFF12b886)
                    )
                }
            }

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
                ItemMenuButton(
                    expanded = expanded,
                    onExpandedChange = { value -> expanded = !expanded },
                    onDelete = { deleteShowAlert = true },
                    onEdite = { onEdit(scanItem) }
                )
            }
        }
    )

    JsConfirm(
        show = deleteShowAlert,
        message = "정말 삭제 하시겠습니까?",
        onConfirm = {
            onDelete(scanItem)
            deleteShowAlert = false
        },
        onCancel = {
            deleteShowAlert = false
        }
    )
}