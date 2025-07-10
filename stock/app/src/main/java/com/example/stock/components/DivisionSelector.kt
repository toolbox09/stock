package com.example.stock.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DivisionSelector (
    valueList: List<String> = listOf<String>("가","나","다", "라","마","바","사","아","자","차","카","타","파","하"),
    initialIndex: Int = 0,
    onValueChange: (String) -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(initialIndex) }
    val currentValue = valueList[selectedIndex]

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedIconButton(
            onClick = {
                if (selectedIndex > 0) {
                    selectedIndex--
                    onValueChange(valueList[selectedIndex])
                }
            },
            modifier = Modifier.size(24.dp),
            enabled = selectedIndex > 0
        ) {
            Icon(Icons.Default.ArrowLeft, contentDescription = "이전")
        }

        Text(
            text = currentValue,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        OutlinedIconButton(
            onClick = {
                if (selectedIndex < valueList.size - 1) {
                    selectedIndex++
                    onValueChange(valueList[selectedIndex])
                }
            },
            modifier = Modifier.size(24.dp),
            enabled = selectedIndex < valueList.size - 1
        ) {
            Icon(Icons.Default.ArrowRight, contentDescription = "다음")
        }
    }
}