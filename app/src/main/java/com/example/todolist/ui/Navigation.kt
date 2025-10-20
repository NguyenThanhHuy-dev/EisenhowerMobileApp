package com.example.todolist.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.ui.tasks.AddTaskScreen
import com.example.todolist.ui.tasks.EisenhowerScreen
import com.example.todolist.ui.tasks.FinishedTasksScreen
import com.example.todolist.ui.tasks.getTaskViewModel
import com.example.todolist.ui.viewmodels.TaskViewModel
import com.example.todolist.ui.tasks.TaskDetailScreen

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
                onAddTask = { navController.navigate("addTask") },
                onViewFinishedTasks = { navController.navigate("finishedTasks") },
                onTaskClick = { taskId ->
                    navController.navigate("taskDetail/$taskId")
                }
            )
        }
        composable("addTask") {
            AddTaskScreen(
                viewModel = taskViewModel,
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
