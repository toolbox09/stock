package com.example.stock.screens.work.widget

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.stock.components.BottomBarButton
import com.example.stock.R
import com.example.stock.components.JsConfirm

@Composable
fun WorkCheckedBottomBar(
    onDelete : () -> Unit = {},
    onDeleteAll : () -> Unit = {},
    onUpload : () -> Unit = {},
    onUploadAll : () -> Unit = {},
    onAddLocation : () -> Unit = {},
) {
    var isShowDelete by remember { mutableStateOf(false) }
    var isShowDeleteAll by remember { mutableStateOf(false) }

    BottomAppBar {
        BottomBarButton(
            onClick = onUpload,
            text = "선택 업로드",
            icon = ImageVector.vectorResource( id = R.drawable.export ),
            modifier = Modifier.weight(1f),
        )
        BottomBarButton(
            onClick = onUploadAll,
            text = "전체 업로드",
            icon = ImageVector.vectorResource( id = R.drawable.export_all ),
            modifier = Modifier.weight(1f),
        )
        BottomBarButton(
            onClick = onAddLocation,
            text = "추가",
            icon =  ImageVector.vectorResource( id = R.drawable.add ),
            modifier = Modifier.weight(1f),
        )
        BottomBarButton(
            onClick = { isShowDelete = true },
            text = "선택 삭제",
            icon = Icons.Outlined.Clear,
            modifier = Modifier.weight(1f),
        )
        BottomBarButton(
            onClick = { isShowDeleteAll = true },
            text = "선택 삭제",
            icon = ImageVector.vectorResource( id = R.drawable.eraser),
            modifier = Modifier.weight(1f),
        )
    }

    JsConfirm(
        show = isShowDelete,
        message = "정말 삭제 하시겠습니까?",
        onConfirm = {
            onDelete()
            isShowDelete = false
        },
        onCancel = {
            isShowDelete = false
        }
    )

    JsConfirm(
        show = isShowDeleteAll,
        message = "정말 모두 삭제 하시겠습니까?",
        onConfirm = {
            onDeleteAll()
            isShowDeleteAll = false
        },
        onCancel = {
            isShowDeleteAll = false
        }
    )
}