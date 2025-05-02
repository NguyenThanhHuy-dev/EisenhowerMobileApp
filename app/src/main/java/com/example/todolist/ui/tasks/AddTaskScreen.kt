package com.example.todolist.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.todolist.data.Task
import com.example.todolist.ui.viewmodels.TaskViewModel
import androidx.compose.ui.graphics.Color // ✅ Correct import
import androidx.compose.ui.graphics.Brush
import com.example.todolist.GreetingImage
import com.example.todolist.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = getTaskViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isImportant by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
//            TopAppBar(
//                title = { Text("Add New Task", color = Color(0xE6FF4B4B)) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                },
//
//            )
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add new task",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier.shadow(elevation = 6.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },

    ) { padding ->
        GreetingImage(imageResId = R.drawable.wave1, alpha = 0.5f)


        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFF5DE0F0),
////                            Color(0xFF77D6F1),
////                            Color(0xFF90CDF2),
//                            Color(0xFFAAC3F3)
//                        )
//                    )
//                )
        )
        {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),

                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)", color = Color.Black) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Eisenhower Matrix Classification",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Important?", color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isImportant,
                        onCheckedChange = { isImportant = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Urgent?", color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val task = Task(
                                title = title,
                                description = description,
                                isImportant = isImportant,
                                isUrgent = isUrgent
                            )
                            viewModel.addTask(task)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank()
                ) {
                    Text("Add Task", color = Color.Black)
                }
            }
        }
    }
}