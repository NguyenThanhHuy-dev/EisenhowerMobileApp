package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.feature.tasks.ui.AddTaskScreen
import com.example.todolist.feature.tasks.ui.EisenhowerScreen
import com.example.todolist.feature.tasks.ui.FinishedTasksScreen
import com.example.todolist.feature.tasks.ui.getTaskViewModel
import com.example.todolist.feature.tasks.TaskViewModel
import com.example.todolist.feature.tasks.ui.TaskDetailScreen

@Composable
fun EisenhowerNavigation() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = getTaskViewModel()

    NavHost(
        navController = navController,
        startDestination = "eisenhower"
    ) {
        composable("eisenhower") {
            EisenhowerScreen(
                viewModel = taskViewModel,
                onAddTask = { important, urgent ->
                    navController.navigate("addTask?important=$important&urgent=$urgent")
                },
                onViewFinishedTasks = { navController.navigate("finishedTasks") },
                onTaskClick = { taskId ->
                    navController.navigate("taskDetail/$taskId")
                }
            )
        }
        composable(
            route = "addTask?important={important}&urgent={urgent}",
            arguments = listOf(
                navArgument("important") { type = NavType.BoolType; defaultValue = false },
                navArgument("urgent") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            AddTaskScreen(
                viewModel = taskViewModel,
                initialImportant = backStackEntry.arguments?.getBoolean("important") ?: false,
                initialUrgent = backStackEntry.arguments?.getBoolean("urgent") ?: false,
                onBack = { navController.popBackStack() }
            )
        }
        // EisenhowerNavigation.kt
        composable("finishedTasks") {
            FinishedTasksScreen(
                viewModel = taskViewModel,
                onBack = { navController.popBackStack() },
                onTaskClick = { taskId ->
                    navController.navigate("taskDetail/$taskId")
                }
            )
        }

        composable(
            route = "taskDetail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
            if (taskId != null) {
                TaskDetailScreen(
                    taskId = taskId,
                    viewModel = taskViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
