package com.example.stock.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun JsConfirm(
    show: Boolean,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String = "알림",
    confirmText: String = "확인",
    cancelText: String = "취소"
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(cancelText)
                }
            },
            title = { Text(title) },
            text = { Text(message) }
        )
    }
}