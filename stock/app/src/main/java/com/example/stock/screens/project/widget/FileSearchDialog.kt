package com.example.stock.screens.project.widget
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.stock.entities.FileInfo


private data class DialogUiState(
    val isLoading: Boolean = true,
    val files: List<FileInfo> = emptyList(),
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileSearchDialog(
    onDismiss: () -> Unit,
    onFileSelected: (String) -> Unit,
    getFiles: suspend () -> Result<List<FileInfo>>
) {
    var uiState by remember { mutableStateOf(DialogUiState()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        getFiles().onSuccess { files ->
            uiState = DialogUiState(isLoading = false, files = files)
        }.onFailure { error ->
            uiState = DialogUiState(isLoading = false, error = error.message ?: "알 수 없는 오류")
        }
    }

    val filteredFiles = if (searchQuery.isBlank()) {
        uiState.files
    } else {
        uiState.files.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("파일 선택") },
                    navigationIcon = { IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, "닫기") } }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                // 텍스트 필드와 같은 UI 요소는 그대로 사용 [1]
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("검색") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    singleLine = true
                )

                // 자체 상태(uiState)에 따라 UI를 분기 처리
                when {
                    uiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("오류: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(filteredFiles, key = { it.name }) { file ->
                                Text(
                                    text = file.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onFileSelected(file.name) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
