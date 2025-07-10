package com.example.stock.screens.location.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.PlaylistRemove
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.stock.components.JsConfirm


@Composable
fun LocationBottomAppBar(
    previousLocationId : Long?,
    checkedIds : Set<Long>,
    onBack : () -> Unit = {},
    onForward : () -> Unit = {},
    onAdd : () -> Unit = {},
    onDelete : () -> Unit = {},
    onDeleteAll : () -> Unit = {},
){
    var isShowDelete by remember { mutableStateOf(false) }
    var isShowDeleteAll by remember { mutableStateOf(false) }

    BottomAppBar {

        IconButton(
            onClick = { onBack() },
            modifier = Modifier.weight(1f),
            enabled = previousLocationId != null,
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Localized description")
        }
        IconButton(
            onClick = { onForward() },
            modifier = Modifier.weight(1f),
            // enabled = nextLocationId != null,
        ) {
            Icon(Icons.Outlined.ArrowForward, contentDescription = "Localized description")
        }
        IconButton(
            onClick = { onAdd() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.AddCircle, contentDescription = "Localized description")
        }
        IconButton(
            onClick = { isShowDelete = true },
            enabled = checkedIds.isNotEmpty(),
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = "Localized description")
        }
        IconButton(
            onClick = { isShowDeleteAll = true },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.PlaylistRemove, contentDescription = "Localized description")
        }
    }

    JsConfirm(
        show = isShowDelete,
        message = "${checkedIds.size}개를 삭제 하시겠습니까?",
        onConfirm = {
            isShowDelete = false
            onDelete()
        },
        onCancel = {
            isShowDelete = false
        }
    )

    JsConfirm(
        show = isShowDeleteAll,
        message = "정말 모두 삭제 하시겠습니까?",
        onConfirm = {
            isShowDeleteAll = false
            onDeleteAll()
        },
        onCancel = {
            isShowDeleteAll = false
        }
    )
}