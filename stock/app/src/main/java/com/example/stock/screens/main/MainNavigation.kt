package com.example.stock.screens.main

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stock.screens.bluetooth.BluetoothModal
import com.example.stock.screens.bluetooth.BluetoothScreen
import com.example.stock.screens.location.LocationScreen
import com.example.stock.screens.project.ProjectCreateScreen
import com.example.stock.screens.project.ProjectDetailScreen
import com.example.stock.screens.project.ProjectScreen
import com.example.stock.screens.root.RootViewModel
import com.example.stock.screens.work.ScanItemSearchScreen
import com.example.stock.screens.work.WorkScreen
import kotlinx.coroutines.launch


enum class MainNavRoute(
    val route : String,
) {
    Work("work"),
    Location("location/locationId"),
    Project("project"),
    ProjectDetail("projectDetail/projectName"),
    ProjectCreate("projectCreate"),
    Bluetooth("bluetooth"),
    BluetoothModal("bluestocking"),
    ScanItemSearch("scanItemSearch")
}

@Composable
fun MainNavigation( rootViewModel: RootViewModel ) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItem = MainDrawerItem.entries.find { it.route == currentRoute }
        ?: MainDrawerItem.Work

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainDrawer(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                },
                onLogout = { rootViewModel.logout() }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = MainNavRoute.Work.route
        ) {
            composable(MainNavRoute.Work.route) {
                WorkScreen(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(MainNavRoute.Project.route) {
                ProjectScreen(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(MainNavRoute.Bluetooth.route) {
                BluetoothScreen(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }

            composable(MainNavRoute.BluetoothModal.route) {
                BluetoothModal (
                    onMenuClick = { navController.popBackStack() }
                )
            }

            composable(MainNavRoute.ProjectCreate.route) {
                ProjectCreateScreen(
                    onNavigateBack = { navController.navigate(MainNavRoute.Project.route) }
                )
            }

            composable(MainNavRoute.ScanItemSearch.route) {
                ScanItemSearchScreen(
                    navController = navController,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "location/{locationId}",
                arguments = listOf(navArgument("locationId") { type = NavType.LongType })
            ) { backStackEntry ->
                val locationId = backStackEntry.arguments?.getLong("locationId")
                if(locationId != null) {
                    LocationScreen(
                        locationId = locationId,
                        navController = navController,
                        onMenuClick = { navController.navigate(MainNavRoute.Work.route) }
                    )
                }
            }

            composable(
                route = "projectDetail/{projectName}",
                arguments = listOf(navArgument("projectName") { type = NavType.StringType })
            ) { backStackEntry ->
                val projectName = backStackEntry.arguments?.getString("projectName")
                if(projectName != null) {
                    ProjectDetailScreen(
                        projectName = projectName,
                        onBackClick = { navController.navigate(MainNavRoute.Project.route) }
                    )
                }
            }
        }
    }
}