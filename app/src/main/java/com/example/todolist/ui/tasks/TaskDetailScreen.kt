package com.example.todolist.ui.tasks

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.todolist.ui.viewmodels.TaskViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Lấy task từ ViewModel và theo dõi sự thay đổi của nó
    val taskState by viewModel.getTaskById(taskId).collectAsState(initial = null)

    // Chỉ hiển thị giao diện khi task đã được tải xong
    taskState?.let { task ->
        var title by remember(task.title) { mutableStateOf(task.title) }
        var description by remember(task.description) { mutableStateOf(task.description) }
        var isImportant by remember(task.isImportant) { mutableStateOf(task.isImportant) }
        var isUrgent by remember(task.isUrgent) { mutableStateOf(task.isUrgent) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Edit Task") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Các switch Important và Urgent
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Important?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isImportant, onCheckedChange = { isImportant = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Urgent?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isUrgent, onCheckedChange = { isUrgent = it })
                }
                Spacer(modifier = Modifier.weight(1f)) // Đẩy nút Save xuống dưới

                // Nút Save
                Button(
                    onClick = {
                        val updatedTask = task.copy(
                            title = title,
                            description = description,
                            isImportant = isImportant,
                            isUrgent = isUrgent
                        )
                        viewModel.updateTask(updatedTask)
                        onBack() // Quay lại màn hình trước sau khi lưu
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank()
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}