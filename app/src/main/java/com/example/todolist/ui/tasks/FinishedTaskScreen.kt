package com.example.todolist.ui.tasks

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.todolist.ui.components.TaskCard
import com.example.todolist.ui.viewmodels.TaskViewModel

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedTasksScreen(
    onBack: () -> Unit,
    viewModel: TaskViewModel,
    onTaskClick: (Long) -> Unit
) {
    val tasks by viewModel.completedTasks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Finished Tasks") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onClick = { onTaskClick(task.id) },
                    onCheckedChange = { /* maybe uncheck to mark as not done */ },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }
    }
}
