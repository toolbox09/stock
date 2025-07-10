package com.example.stock.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.stock.R


object IconSet {

    @Composable
    fun ResourceIcon(
        id : Int,
        modifier : Modifier = Modifier,
        contentDescription : String? = null,
    ) {
        Icon(
            painter  = painterResource(id = id),
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }

    @Composable
    fun Logo(modifier: Modifier = Modifier, function: @Composable () -> Unit) {
        ResourceIcon(id = R.drawable.box, modifier = modifier)
    }


    @Composable
    fun Smile(modifier: Modifier = Modifier, function: @Composable () -> Unit) {
        ResourceIcon(id = R.drawable.sticker_smile, modifier = modifier)
    }


    @Composable
    fun Add(modifier: Modifier = Modifier, function: @Composable () -> Unit) {
        ResourceIcon(id = R.drawable.add, modifier = modifier)
    }

    @Composable
    fun ExportAll(modifier: Modifier = Modifier, function: @Composable () -> Unit) {
        ResourceIcon(id = R.drawable.export_all, modifier = modifier)
    }

    @Composable
    fun Eraser(modifier: Modifier = Modifier, function: @Composable () -> Unit) {
        ResourceIcon(id = R.drawable.eraser, modifier = modifier)
    }
}