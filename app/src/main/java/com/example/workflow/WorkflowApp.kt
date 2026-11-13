package com.example.workflow

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.workflow.ui.screens.*
import com.example.workflow.ui.viewmodel.WorkflowViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Workflows : Screen("workflows", "Workflows", Icons.Default.Folder)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.BarChart)
    object Detail : Screen("workflow_detail/{workflowId}", "Detail", Icons.Default.Info) {
        fun createRoute(workflowId: Long) = "workflow_detail/$workflowId"
    }
}

@Composable
fun WorkflowApp(viewModel: WorkflowViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        Screen.Dashboard,
        Screen.Workflows,
        Screen.Analytics
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in items.map { it.route }) {
                NavigationBar(modifier = Modifier.testTag("bottom_navigation")) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("nav_item_${screen.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToWorkflows = { navController.navigate(Screen.Workflows.route) },
                    onWorkflowClick = { workflowId ->
                        navController.navigate(Screen.Detail.createRoute(workflowId))
                    }
                )
            }
            composable(Screen.Workflows.route) {
                WorkflowListScreen(
                    viewModel = viewModel,
                    onWorkflowClick = { workflowId ->
                        navController.navigate(Screen.Detail.createRoute(workflowId))
                    }
                )
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(viewModel = viewModel)
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("workflowId") { type = NavType.LongType })
            ) { backStackEntry ->
                val workflowId = backStackEntry.arguments?.getLong("workflowId") ?: 0L
                WorkflowDetailScreen(
                    workflowId = workflowId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
