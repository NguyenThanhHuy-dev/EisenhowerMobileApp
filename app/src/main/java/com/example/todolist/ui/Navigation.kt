package com.example.todolist.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.tasks.AddTaskScreen
import com.example.todolist.ui.tasks.EisenhowerScreen
import com.example.todolist.ui.tasks.FinishedTasksScreen
import com.example.todolist.ui.tasks.getTaskViewModel
import com.example.todolist.ui.viewmodels.TaskViewModel

@Composable
fun EisenhowerNavigation() {
    val navController = rememberNavController()

    val taskViewModel : TaskViewModel = getTaskViewModel()

    NavHost(
        navController = navController,
        startDestination = "eisenhower"
    ) {
        composable("eisenhower") {
            EisenhowerScreen(
                viewModel = taskViewModel,
                onAddTask = { navController.navigate("addTask") },
                onViewFinishedTasks = { navController.navigate("finishedTasks") } // <-- this fixes the issue
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
                onBack = { navController.popBackStack() }
            )
        }
    }
}
