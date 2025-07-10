package com.example.stock.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stock.entities.ProjectInfo
import com.example.stock.screens.main.MainNavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    navController : NavHostController,
    onMenuClick: () -> Unit
) {
    val viewModel: ProjectViewModel = viewModel(factory = ProjectViewModel.Factory)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProjects()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로젝트",  fontSize = 15.sp, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                },
                actions = {
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(MainNavRoute.ProjectCreate.route) }) {
                Icon(Icons.Default.Add, contentDescription = "생성")
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // 에러 메시지
            state.error != null -> {
                Card(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = state.error!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.getProjects()
                    }
                ) {
                    Text("다시 시도")
                }
            }

            else -> {
                if (state.projects != null) {
                    if (state.projects!!.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("프로젝트가 없습니다")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.projects!!) { project ->
                                ProjectStateItem(
                                    project = project,
                                    onClick = {
                                        navController.navigate("projectDetail/${project.name}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProjectStateItem(
    project: ProjectInfo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            project.created?.let { created ->
                Text(
                    text = "생성일: $created",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            project.masterUrl?.let { url ->
                Text(
                    text = "Master URL: $url",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalDivider()
    }
}