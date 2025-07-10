package com.example.stock.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stock.api.dto.CreateProjectReq
import com.example.stock.domain.repositories.FileSearchRepository
import com.example.stock.screens.project.widget.FileSearchDialog
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateScreen(
    onNavigateBack: () -> Unit,
    repository: FileSearchRepository = FileSearchRepository()
) {
    var projectName by remember { mutableStateOf("") }
    var selectedFile1 by remember { mutableStateOf<String?>(null) }
    var selectedFile2 by remember { mutableStateOf<String?>(null) }

    var dialogTarget by remember { mutableStateOf<Int?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope() // 코루틴 스코프 생성


    if (dialogTarget != null) {
        FileSearchDialog(
            onDismiss = { dialogTarget = null },
            onFileSelected = { fileName ->
                when (dialogTarget) {
                    1 -> selectedFile1 = fileName
                    2 -> selectedFile2 = fileName
                }
                dialogTarget = null
            },
            getFiles = {
                if (dialogTarget == 1) {
                    repository.getMasterList()
                } else {
                    repository.getMatchList()
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("새 프로젝트 생성") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("프로젝트명") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            FileSelector(
                label = "마스터 파일",
                selectedFileName = selectedFile1 ?: "파일을 선택하세요",
                onClick = { dialogTarget = 1 }
            )

            // 파일 2 선택 컨트롤
            FileSelector(
                label = "전산 현황",
                selectedFileName = selectedFile2 ?: "파일을 선택하세요",
                onClick = { dialogTarget = 2 }
            )

            Spacer(Modifier.weight(1f)) // 버튼을 하단에 위치시키기 위한 Spacer

            // 프로젝트 생성 버튼 (예시)
            Button(
                // 모든 필드가 채워지고, 로딩 중이 아닐 때만 활성화
                enabled = projectName.isNotBlank() && !isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            repository.createProject(
                                CreateProjectReq(
                                    name = projectName,
                                    masterUrl = selectedFile1,
                                    matchUrl = selectedFile2
                                )
                            ).onSuccess { projectId ->
                                snackbarHostState.showSnackbar(
                                    "프로젝트가 성공적으로 생성되었습니다."
                                )
                                // 성공 시 필드 초기화
                                projectName = ""
                                selectedFile1 = null
                                selectedFile2 = null
                            }.onFailure { error ->
                                snackbarHostState.showSnackbar(
                                    "오류 발생: ${error.message}"
                                )
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("프로젝트 생성")
                }
            }
        }
    }
}

@Composable
fun FileSelector(
    label: String,
    selectedFileName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        OutlinedTextField(
            value = selectedFileName,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "파일 선택"
                )
            },
            // 비활성화 상태의 색상을 활성화 상태처럼 보이도록 재정의
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }

}