package com.example.stock.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


private val StockColorScheme = lightColorScheme(
    primary = StockPrimary,
    onPrimary = StockPrimaryForeground,
    primaryContainer = StockPrimary,
    onPrimaryContainer = StockPrimaryForeground,

    secondary = StockSecondary,
    onSecondary = StockSecondaryForeground,
    secondaryContainer = StockSecondary,
    onSecondaryContainer = StockSecondaryForeground,

    tertiary = StockBorder,
    onTertiary = StockForeground,
    tertiaryContainer = StockBorder,
    onTertiaryContainer = StockForeground,

    background = StockBackground,
    onBackground = StockForeground,
    surface = StockBackground,
    onSurface = StockForeground,

    surfaceVariant = StockBackground,
    onSurfaceVariant = StockForeground,
    surfaceContainer = StockCard,
    surfaceContainerLow = StockCard,
    surfaceContainerHigh = StockCard,
    surfaceContainerHighest = StockCard,

    outline = StockBorder,
    outlineVariant = StockBorder,

    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFECACA),
    onErrorContainer = Color(0xFF991B1B),

    surfaceTint = Color.Transparent,
    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = StockForeground,
    inverseOnSurface = StockBackground,
    inversePrimary = StockBackground
)

val StockShapes = Shapes(
    small = RoundedCornerShape(8.dp),   // 버튼, 칩 등 작은 컴포넌트
    medium = RoundedCornerShape(12.dp), // 카드, 다이얼로그 등 중간 크기 컴포넌트
    large = RoundedCornerShape(16.dp)   // 바텀 시트 등 큰 컴포넌트
)

@Composable
fun StockTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = StockColorScheme,
        typography = Typography,
        shapes = StockShapes,
        content = content
    )
}