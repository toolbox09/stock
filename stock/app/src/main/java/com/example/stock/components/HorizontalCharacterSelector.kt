package com.example.stock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. 표시할 데이터 정의
val hangulCharacters = listOf(
    "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하"
)

@Composable
fun HorizontalCharacterSelector(
    initCharacter : String,
    onItemSelected: (String) -> Unit,
    items: List<String> = hangulCharacters,
) {
    // 2. 선택된 항목을 기억하고 UI를 갱신하기 위한 State
    val initialIndex = items.indexOf(initCharacter).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    var selectedItem by remember { mutableStateOf(initCharacter) }

    LaunchedEffect(Unit) {
        onItemSelected(selectedItem)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 아이템 사이의 간격
        contentPadding = PaddingValues(horizontal = 16.dp) // 리스트의 좌우 패딩
    ) {
        items(items) { item ->
            CharacterItem(
                character = item,
                isSelected = (item == selectedItem),
                onItemClick = { clickedItem ->
                    selectedItem = clickedItem // 상태 업데이트
                    onItemSelected(clickedItem) // 부모에게 선택된 값 전달
                }
            )
        }
    }
}

/**
 * 개별 문자 아이템 UI
 * @param character 표시할 문자
 * @param isSelected 현재 아이템이 선택되었는지 여부
 * @param onItemClick 아이템 클릭 시 호출될 람다 함수
 */
@Composable
fun CharacterItem(
    character: String,
    isSelected: Boolean,
    onItemClick: (String) -> Unit
) {
    // 5. 선택 상태에 따라 다른 스타일 적용
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(height = 30.dp, width = 50.dp) // 아이템 크기 정사각형으로 고정
            .clip(RoundedCornerShape(12.dp)) // 모서리를 둥글게
            .background(backgroundColor)
            .clickable { onItemClick(character) }, // 클릭 이벤트 처리
        contentAlignment = Alignment.Center // 내용물(텍스트)을 중앙에 배치
    ) {
        Text(
            text = character,
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

