package com.example.stock.screens.location.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScanItemBottomSheet(
    show: Boolean,
    scanItemId : Long,
    initialBarcode: String = "",
    initialCount: String = "",
    onSave: ( scanItemId : Long, barcode: String, count: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var barcode by remember { mutableStateOf(initialBarcode) }
    var count by remember { mutableStateOf(initialCount) }
    var isValidInput by remember { mutableStateOf(false) }

    // 초기값 설정
    LaunchedEffect(initialBarcode, initialCount) {
        barcode = initialBarcode
        count = initialCount
    }

    // 입력값 검증
    LaunchedEffect(barcode, count) {
        isValidInput = barcode.isNotBlank() && count.toIntOrNull() != null && count.toInt() > 0
    }

    if (show) {
        ModalBottomSheet(
            onDismissRequest = {
                barcode = initialBarcode
                count = initialCount
                onDismiss()
            },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxHeight(0.9f),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "스캔 항목 편집",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Button(
                        onClick = {
                            onSave( scanItemId ,barcode, count.toInt())
                            onDismiss()
                        },
                        enabled = isValidInput
                    ) {
                        Text("저장")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 바코드 입력
                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it.uppercase(Locale.getDefault()) },
                    label = { Text("바코드") },
                    placeholder = { Text("바코드를 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // 수량 입력
                OutlinedTextField(
                    value = count,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            count = newValue
                        }
                    },
                    label = { Text("수량") },
                    placeholder = { Text("수량을 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // 버튼 영역
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    /*
                    TextButton(
                        onClick = {
                            barcode = initialBarcode
                            count = initialCount
                            onDismiss()
                        }
                    ) {
                        Text("취소")
                    }
                    Spacer(Modifier.width(8.dp))

                     */
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
