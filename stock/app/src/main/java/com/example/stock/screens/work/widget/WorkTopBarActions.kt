package com.example.stock.screens.work.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.stock.components.JsConfirm

@Composable
fun WorkTopBarActions(
    onStartWork : () -> Unit,
    onCloseWork : () -> Unit,
    onSearch : () -> Unit,
) {    var expanded by remember { mutableStateOf(false) }
    var isShowOpenWork by remember { mutableStateOf(false) }
    var isShowCloseWork by remember { mutableStateOf(false) }

    IconButton(onClick = { onSearch() }) {
        Icon(Icons.Default.Search, contentDescription = "검색")
    }
    WorkMenuButton(
        expanded = expanded,
        onExpandedChange = { value -> expanded = !expanded },
        onNewWork = {
            isShowOpenWork = true
        },
        onCloseWork = {
            isShowCloseWork = true
        }
    )

    JsConfirm(
        show = isShowOpenWork,
        message = "작업내용이 삭제됩니다, 계속 진행하시겠습니까?",
        onConfirm = {
            onStartWork()
            isShowOpenWork = false
            expanded = false
        },
        onCancel = {
            isShowOpenWork = false
            expanded = false
        }
    )

    JsConfirm(
        show = isShowCloseWork,
        message = "정말 종료 하시겠습니까?",
        onConfirm = {
            onCloseWork()
            isShowCloseWork = false
            expanded = false
        },
        onCancel = {
            isShowCloseWork = false
            expanded = false
        }
    )
}