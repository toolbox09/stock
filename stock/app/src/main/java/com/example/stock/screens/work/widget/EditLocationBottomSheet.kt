package com.example.stock.screens.work.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stock.components.DivisionSelector
import com.example.stock.components.HorizontalCharacterSelector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLocationBottomSheet(
    show: Boolean,
    locationId : Long,
    initDivision : String,
    initName: String,
    onSave: ( locationId : Long, initDivision: String, initName: String) -> Unit,
    onDismiss: () -> Unit
) {
    var division by remember { mutableStateOf(initDivision) }
    var name by remember { mutableStateOf(initName) }
    var isValidInput by remember { mutableStateOf(false) }

    // 초기값 설정
    LaunchedEffect(initDivision, initName) {
        division = initDivision
        name = initName
    }

    // 입력값 검증
    LaunchedEffect(division, name) {
        isValidInput = division.isNotBlank() && name.isNotBlank()
    }

    if (show) {
        ModalBottomSheet(
            onDismissRequest = {
                division = initDivision
                name = initName
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
                        text = "로케이션 편집",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Button(
                        onClick = {
                            onSave( locationId ,division, name)
                            onDismiss()
                        },
                        enabled = isValidInput
                    ) {
                        Text("저장")
                    }
                }

                Spacer(Modifier.height(16.dp))

                HorizontalCharacterSelector( initCharacter = division, onItemSelected = { value -> division = value })

                // 바코드 입력
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("로케이션 명") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
