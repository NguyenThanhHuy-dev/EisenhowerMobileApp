// app/src/main/java/com/example/todolist/ui/tasks/EisenhowerScreen.kt
package com.example.todolist.ui.tasks

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.ui.components.MyBottomBar
import com.example.todolist.ui.components.Quadrant
import com.example.todolist.ui.theme.AmberYellow
import com.example.todolist.ui.theme.CoralPink
import com.example.todolist.ui.theme.DarkBackground
import com.example.todolist.ui.theme.SilverGray
import com.example.todolist.ui.theme.SkyBlue
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
    val context = LocalContext.current

// --- STATE QUẢN LÝ ẢNH NỀN ---
    var backgroundUri by remember { mutableStateOf(loadBackgroundUri(context)) }
    val backgroundPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // YÊU CẦU GIỮ QUYỀN TRUY CẬP URI LÂU DÀI
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)

            backgroundUri = uri.toString()
            saveBackgroundUri(context, uri.toString())
        }
    }

    // --- STATE QUẢN LÝ AVATAR ---
    var avatarUri by remember { mutableStateOf(loadAvatarUri(context)) }
    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // YÊU CẦU GIỮ QUYỀN TRUY CẬP URI LÂU DÀI
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)

            avatarUri = uri.toString()
            saveAvatarUri(context, uri.toString())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Vòng tròn Avatar
                        AsyncImage(
                            model = avatarUri ?: R.drawable.ic_launcher_foreground, // Ảnh mặc định nếu chưa có avatar
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                                .clickable {
                                    avatarPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        // Lời chào
                        Text(
                            text = getGreeting(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            MyBottomBar(
                onAddTaskClick = onAddTask,
                onFinishedTasksClick = onViewFinishedTasks,
                onChangeBackgroundClick = {
                    backgroundPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

        },
        containerColor = DarkBackground

    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = backgroundUri
                    ?: R.drawable.background1, // Nếu URI có, dùng URI, nếu không dùng ảnh mặc định
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground.copy(alpha = 0.5f)) // Lớp phủ màu tối, bán trong suốt
            )

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
                            backgroundColor = CoralPink
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
                            backgroundColor = SkyBlue

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
                            backgroundColor = AmberYellow

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
                            backgroundColor = SilverGray

                        )
                    }
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
// --- CÁC HÀM HELPER ĐỂ LƯU/TẢI URI ---
private fun saveBackgroundUri(context: Context, uriString: String) {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("background_uri", uriString)
        apply()
    }
}

private fun loadBackgroundUri(context: Context): String? {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("background_uri", null)
}

// --- THÊM CÁC HÀM HELPER MỚI CHO AVATAR ---
private fun saveAvatarUri(context: Context, uriString: String) {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("avatar_uri", uriString)
        apply()
    }
}

private fun loadAvatarUri(context: Context): String? {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("avatar_uri", null)
}