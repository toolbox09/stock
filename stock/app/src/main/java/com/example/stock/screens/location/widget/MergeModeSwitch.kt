package com.example.stock.screens.location.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun MergeModeSwitch(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = { },
    label: String = "병합"
) {
    val isChecked = remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = isChecked.value,
            modifier = Modifier
                .scale(0.7f)
                .padding(8.dp),
            onCheckedChange = { checked ->
                isChecked.value = checked
                onCheckedChange(checked)
            }
        )
        Text(text = label)
        Spacer(modifier = Modifier.padding(4.dp))
    }
}