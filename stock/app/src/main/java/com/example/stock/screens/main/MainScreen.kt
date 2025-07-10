package com.example.stock.screens.main

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import com.example.stock.screens.root.RootViewModel

@Composable
fun MainScreen( rootViewModel: RootViewModel ) {
    MainNavigation(rootViewModel)
}