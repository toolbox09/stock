package com.example.stock.screens.project

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stock.components.JsConfirm
import com.example.stock.entities.FileInfo
import com.example.stock.entities.Project
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProjectDetailScreen(
    projectName : String,
    onBackClick: () -> Unit = {},
) {
    val viewModel: ProjectDetailViewModel = viewModel(
        factory = projectDetailViewModelFactory(projectName)
    )
    val state by viewModel.state.collectAsState()
    var isShowCllect by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(projectName) {
        viewModel.refresh()
    }

    LaunchedEffect(state.collectRes) {
        if(state.collectRes != null) {
            scope.launch {
                snackState.showSnackbar(
                    message = "${state.collectRes!!.totalCount}개를 취합 하였습니다",
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
            }
            viewModel.clearResults()
            viewModel.refresh()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackState)
        },
        topBar = {
            TopAppBar(
                title = { Text("",  fontSize = 15.sp, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    Button(onClick = { isShowCllect = true }) {
                        Icon(
                            imageVector = Icons.Filled.AutoFixHigh,
                            contentDescription = "장바구니 아이콘", // 접근성을 위한 설명
                            modifier = Modifier.size(ButtonDefaults.IconSize) // 버튼 기본 아이콘 크기 사용
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("취합")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.project == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("해당 프로젝트가 없습니다.")
                }
            }
            else -> {
                state.project?.let { project ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ProjectBasicInfoSection(project = project)
                        }
                        item {
                            ResultSection( project = project )
                        }
                        item {
                            WorkFilesSection(workFiles = project.works)
                        }
                    }
                }
            }
        }
    }

    JsConfirm(
        show = isShowCllect,
        message = "취합 하시겠습니까?",
        onConfirm = {
            viewModel.collect()
            isShowCllect = false
        },
        onCancel = {
            isShowCllect = false
        }
    )
}

@Composable
fun ProjectBasicInfoSection(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "기본 정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InfoRow(
                label = "프로젝트명",
                value = project.name,
                icon = Icons.Outlined.Folder
            )

             InfoRow(
                label = "생성일",
                value = project.created,
                icon = Icons.Outlined.Event
            )

            project.masterUrl?.let { masterUrl ->
                InfoRow(
                    label = "마스터",
                    value = masterUrl.toString(),
                    icon = Icons.Outlined.Checklist
                )
            }

            project.matchUrl?.let { matchUrl ->
                InfoRow(
                    label = "전산",
                    value = matchUrl,
                    icon = Icons.Outlined.Inventory
                )
            }

        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.width(80.dp),
            maxLines = 1,
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            modifier = Modifier.weight(1f),
            maxLines = 1,
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ResultSection(project: Project){
    if(project.match == null && project.merge == null ) return


    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "취합 정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            project.merge?.let {
                InfoRow(
                    label = "취합",
                    value = project.merge.name,
                    icon = Icons.Outlined.InsertDriveFile
                )
            }

            project.match?.let {
                InfoRow(
                    label = "매칭",
                    value = project.match.name,
                    icon = Icons.Outlined.InsertDriveFile
                )
            }
        }
    }
}

@Composable
fun WorkFilesSection(workFiles: List<FileInfo>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "작업 파일",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Badge {
                    Text(text = "${workFiles.size}")
                }
            }

            if (workFiles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "작업 파일이 없습니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                workFiles.forEach { file ->
                    WorkFileItem(file = file)
                }
            }
        }
    }
}

@Composable
fun WorkFileItem(file: FileInfo) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.InsertDriveFile,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(onClick = { /* TODO: 파일 다운로드 또는 상세보기 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "더보기"
                )
            }
        }
    }
}