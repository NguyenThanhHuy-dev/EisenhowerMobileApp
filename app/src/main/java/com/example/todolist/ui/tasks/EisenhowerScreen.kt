// app/src/main/java/com/example/todolist/ui/tasks/EisenhowerScreen.kt
package com.example.todolist.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.GreetingImage
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.ui.components.MyBottomBar
import com.example.todolist.ui.components.Quadrant
import com.example.todolist.ui.theme.DelegateColor
import com.example.todolist.ui.theme.DoFirstColor
import com.example.todolist.ui.theme.DontDoColor
import com.example.todolist.ui.theme.ScheduleColor
import com.example.todolist.ui.viewmodels.TaskViewModel
import com.example.todolist.ui.viewmodels.TaskViewModelFactory
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EisenhowerScreen(
    onAddTask: () -> Unit,
    onViewFinishedTasks: () -> Unit,
    modifier: Modifier = Modifier,
    onTaskClick: (Long) -> Unit,
    viewModel: TaskViewModel,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = getGreeting(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            MyBottomBar(
                onAddTaskClick = onAddTask,
                onFinishedTasksClick = onViewFinishedTasks
            )

        },
        containerColor = Color(0xFF1A202C) // Dark background

        ) { padding ->
        GreetingImage(imageResId = R.drawable.background1, alpha = 0.3f)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Row 1: Quadrant 1 and 2
                Row(modifier = Modifier.weight(1f)) {
                    // Quadrant 1: Urgent & Important
                    val q1Tasks by viewModel.quadrant1Tasks.collectAsState(initial = emptyList())
                    Quadrant(
                        title = "Do First",
                        tasks = q1Tasks,
                        onTaskClick = onTaskClick,

                        onTaskChecked = { task, checked ->
                            viewModel.updateTask(task.copy(isCompleted = checked))
                        },
                        onDeleteTask = { task ->
                            viewModel.deleteTask(task)
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = DoFirstColor
                    )

                    // Quadrant 2: Not Urgent & Important
                    val q2Tasks by viewModel.quadrant2Tasks.collectAsState(initial = emptyList())
                    Quadrant(
                        title = "Schedule",
                        tasks = q2Tasks,
                        onTaskClick = onTaskClick,

                        onTaskChecked = { task, checked ->
                            viewModel.updateTask(task.copy(isCompleted = checked))
                        },
                        onDeleteTask = { task ->
                            viewModel.deleteTask(task)
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = ScheduleColor

                    )
                }

                // Row 2: Quadrant 3 and 4
                Row(modifier = Modifier.weight(1f)) {
                    // Quadrant 3: Urgent & Not Important
                    val q3Tasks by viewModel.quadrant3Tasks.collectAsState(initial = emptyList())
                    Quadrant(
                        title = "Delegate",
                        tasks = q3Tasks,
                        onTaskClick = onTaskClick,

                        onTaskChecked = { task, checked ->
                            viewModel.updateTask(task.copy(isCompleted = checked))
                        },
                        onDeleteTask = { task ->
                            viewModel.deleteTask(task)
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = DelegateColor

                    )

                    // Quadrant 4: Not Urgent & Not Important
                    val q4Tasks by viewModel.quadrant4Tasks.collectAsState(initial = emptyList())
                    Quadrant(
                        title = "Don't Do",
                        tasks = q4Tasks,
                        onTaskClick = onTaskClick,

                        onTaskChecked = { task, checked ->
                            viewModel.updateTask(task.copy(isCompleted = checked))
                        },
                        onDeleteTask = { task ->
                            viewModel.deleteTask(task)
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = DontDoColor

                    )
                }
            }

        }
    }
}

@Composable
fun getTaskViewModel(): TaskViewModel {
    val context = LocalContext.current
    val application = context.applicationContext as TodoApplication
    return viewModel(
        factory = TaskViewModelFactory(application.repository)
    )
}

// --- Helper Functions ---
fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}
