package com.example.stock.screens.main

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


enum class MainDrawerItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)
{
    Work("재고조사", MainNavRoute.Work.route, Icons.Filled.Home, Icons.Outlined.Home),
    Project("프로젝트", MainNavRoute.Project.route,Icons.Filled.EventAvailable, Icons.Outlined.EventAvailable),
    Bluetooth("플루투스", MainNavRoute.Bluetooth.route, Icons.Filled.Bluetooth, Icons.Outlined.Bluetooth ),
}

@Composable
fun MainDrawer(
    selectedItem: MainDrawerItem,
    onItemSelected: (MainDrawerItem) -> Unit,
    onLogout: () -> Unit
) {
    val items = listOf(
        MainDrawerItem.Work,
        MainDrawerItem.Project,
        MainDrawerItem.Bluetooth,
    )

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = "메뉴",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            items.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(item.title) },
                    selected = selectedItem == item,
                    onClick = { onItemSelected(item) },
                    icon = {
                        Icon(
                            imageVector = if (selectedItem == item) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    badge = {
                        item.badgeCount?.let { count ->
                            Badge {
                                Text(count.toString())
                            }
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()

            NavigationDrawerItem(
                label = { Text("로그아웃") },
                selected = false,
                onClick = onLogout,
                icon = {
                    Icon(Icons.Outlined.PersonOff, contentDescription = "로그아웃")
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}