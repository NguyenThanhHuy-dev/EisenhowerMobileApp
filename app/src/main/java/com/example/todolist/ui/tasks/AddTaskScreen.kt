package com.example.todolist.ui.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolist.GreetingImage
import com.example.todolist.R
import com.example.todolist.data.Task
import com.example.todolist.ui.viewmodels.TaskViewModel
import com.example.todolist.utils.SoundPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isImportant by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add new task",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier.shadow(elevation = 6.dp),

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
                            SoundPlayer.playAddSound()
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