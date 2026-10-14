package com.example.todolist.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.ui.components.TaskCard
import com.example.todolist.ui.viewmodels.TaskViewModel
import com.example.todolist.utils.SoundPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedTasksScreen(
    onBack: () -> Unit,
    viewModel: TaskViewModel,
    onTaskClick: (Long) -> Unit
) {
    val tasks by viewModel.completedTasks.collectAsState(initial = emptyList())
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(text = stringResource(R.string.clear_all_confirm_title)) },
            text = { Text(text = stringResource(R.string.clear_all_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearCompletedTasks()
                        SoundPlayer.playDeleteSound()
                        showClearDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.finished_tasks)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (tasks.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = stringResource(R.string.clear_all_completed),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.no_finished_tasks),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = { onTaskClick(task.id) },
                        onCheckedChange = { checked ->
                            if (!checked) {
                                viewModel.restoreTask(task)
                                SoundPlayer.playAddSound()
                            }
                        },
                        onDelete = {
                            viewModel.deleteTask(task)
                            SoundPlayer.playDeleteSound()
                        }
                    )
                }
            }
        }
    }
}
