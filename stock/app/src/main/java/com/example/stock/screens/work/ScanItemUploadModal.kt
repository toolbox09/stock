package com.example.stock.screens.work

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanItemUploadModal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    locationIds: List<Long>,
) {
    val viewModel: ScanItemUploadViewModel = viewModel(
        factory = scanItemUploadViewModelFactory(locationIds)
    )
    val state by viewModel.state.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            viewModel.refreshForIds(locationIds)
        }
    }




    LaunchedEffect( state.results ) {
        if(state.results != null && state.results!!.isNotEmpty()) {
            scope.launch {
                snackState.showSnackbar(
                    message = "${state.results!!.size}개를 업데이트 하였습니다",
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
            }
            viewModel.clearResults()
            viewModel.refresh()
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.size(width = 32.dp, height = 4.dp)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                UploadHeaderSection(
                    isLoading = state.isLoading,
                    selectedCount = state.scanItems.sumOf { it.count },
                    isAllowReupload = state.isAllowReUpload,
                    onAllowReupload = { viewModel.toggleAllowReUpload() },
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 액션 버튼들
                UploadActionButtons(
                    selectedCount = state.scanItems.sumOf { it.count },
                    isUploading = state.isLoading,
                    onUpload = {
                        viewModel.appendWork()
                        // onUpload(selectedItems.toList())
                    },
                    onCancel = {
                        scope.launch {
                            onDismiss()
                        }
                    }
                )
            }


            SnackbarHost(
                hostState = snackState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun UploadHeaderSection(
    selectedCount: Int,
    isLoading : Boolean,
    isAllowReupload : Boolean,
    onAllowReupload: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "서버 업로드",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "업로드 대상 ${selectedCount}개",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 전체 선택/해제 버튼
            FilterChip(
                onClick = {onAllowReupload()},
                label = {
                    Text(
                        text = "중복 업로드",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = isAllowReupload,
                /*
                leadingIcon = {
                    Icon(
                        imageVector = if (isUploadTarget)
                            Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                */
                //colors = FilterChipDefaults.filterChipColors(
                //    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                //    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                //)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible =  isLoading ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
            )
        }
    }
}

@Composable
fun UploadActionButtons(
    selectedCount: Int,
    isUploading: Boolean,
    onUpload: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 취소 버튼
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            enabled = !isUploading,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp,
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("취소")
        }

        // 업로드 버튼
        Button(
            onClick = onUpload,
            modifier = Modifier.weight(2f),
            enabled = selectedCount > 0 && !isUploading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("업로드 중...")
            } else {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${selectedCount}개 업로드")
            }
        }
    }
}
