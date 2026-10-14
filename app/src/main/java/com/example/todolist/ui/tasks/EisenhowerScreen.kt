package com.example.todolist.ui.tasks

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.data.Task
import com.example.todolist.ui.components.MyBottomBar
import com.example.todolist.ui.components.Quadrant
import com.example.todolist.ui.theme.AmberYellow
import com.example.todolist.ui.theme.CoralPink
import com.example.todolist.ui.theme.DarkBackground
import com.example.todolist.ui.theme.SilverGray
import com.example.todolist.ui.theme.SkyBlue
import com.example.todolist.ui.viewmodels.TaskViewModel
import com.example.todolist.ui.viewmodels.TaskViewModelFactory
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State lưu chế độ xem: Lưới 2x2 vs Tab View (vuốt ngang tối ưu cho Pixel)
    var isGridView by remember { mutableStateOf(loadViewMode(context)) }

    // State quản lý ảnh nền
    var backgroundUri by remember { mutableStateOf(loadBackgroundUri(context)) }
    val backgroundPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)
            backgroundUri = uri.toString()
            saveBackgroundUri(context, uri.toString())
        }
    }

    // State quản lý Avatar
    var avatarUri by remember { mutableStateOf(loadAvatarUri(context)) }
    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)
            avatarUri = uri.toString()
            saveAvatarUri(context, uri.toString())
        }
    }

    // Dữ liệu 4 góc
    val q1Tasks by viewModel.quadrant1Tasks.collectAsState()
    val q2Tasks by viewModel.quadrant2Tasks.collectAsState()
    val q3Tasks by viewModel.quadrant3Tasks.collectAsState()
    val q4Tasks by viewModel.quadrant4Tasks.collectAsState()

    // Hàm xóa có Undo qua Snackbar
    val onDeleteWithUndo: (Task) -> Unit = { task ->
        viewModel.deleteTask(task)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.task_deleted),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.addTask(task)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = avatarUri ?: R.drawable.ic_launcher_foreground,
                            contentDescription = stringResource(R.string.user_avatar),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                                .clickable {
                                    avatarPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = getGreeting(context),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val nextMode = !isGridView
                            isGridView = nextMode
                            saveViewMode(context, nextMode)
                        }
                    ) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridView) stringResource(R.string.tab_view) else stringResource(R.string.grid_view),
                            tint = Color.White
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
                model = backgroundUri ?: R.drawable.background1,
                contentDescription = stringResource(R.string.background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.25f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground.copy(alpha = 0.55f))
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isGridView) {
                    // CHẾ ĐỘ 1: LƯỚI 2x2 CỔ ĐIỂN
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.weight(1f)) {
                            Quadrant(
                                title = stringResource(R.string.do_first),
                                tasks = q1Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                modifier = Modifier.weight(1f),
                                backgroundColor = CoralPink
                            )

                            Quadrant(
                                title = stringResource(R.string.schedule),
                                tasks = q2Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                modifier = Modifier.weight(1f),
                                backgroundColor = SkyBlue
                            )
                        }

                        Row(modifier = Modifier.weight(1f)) {
                            Quadrant(
                                title = stringResource(R.string.delegate),
                                tasks = q3Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                modifier = Modifier.weight(1f),
                                backgroundColor = AmberYellow
                            )

                            Quadrant(
                                title = stringResource(R.string.dont_do),
                                tasks = q4Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                modifier = Modifier.weight(1f),
                                backgroundColor = SilverGray
                            )
                        }
                    }
                } else {
                    // CHẾ ĐỘ 2: 4 TAB VUỐT NGANG (TỐI ƯU TUYỆT ĐỐI CHO MÀN HÌNH NHỎ)
                    val pagerState = rememberPagerState { 4 }
                    val titles = listOf(
                        stringResource(R.string.do_first),
                        stringResource(R.string.schedule),
                        stringResource(R.string.delegate),
                        stringResource(R.string.dont_do)
                    )
                    val colors = listOf(CoralPink, SkyBlue, AmberYellow, SilverGray)

                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        edgePadding = 12.dp,
                        indicator = { tabPositions ->
                            if (pagerState.currentPage < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    color = colors[pagerState.currentPage]
                                )
                            }
                        }
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        color = if (pagerState.currentPage == index) colors[index] else Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) { page ->
                        when (page) {
                            0 -> Quadrant(
                                title = titles[0],
                                tasks = q1Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                backgroundColor = CoralPink
                            )
                            1 -> Quadrant(
                                title = titles[1],
                                tasks = q2Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                backgroundColor = SkyBlue
                            )
                            2 -> Quadrant(
                                title = titles[2],
                                tasks = q3Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                backgroundColor = AmberYellow
                            )
                            3 -> Quadrant(
                                title = titles[3],
                                tasks = q4Tasks,
                                onTaskClick = onTaskClick,
                                onTaskChecked = { task, checked ->
                                    viewModel.updateTask(task.copy(isCompleted = checked))
                                },
                                onDeleteTask = onDeleteWithUndo,
                                backgroundColor = SilverGray
                            )
                        }
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
fun getGreeting(context: Context): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> context.getString(R.string.good_morning)
        in 12..17 -> context.getString(R.string.good_afternoon)
        else -> context.getString(R.string.good_evening)
    }
}

private fun saveViewMode(context: Context, isGridView: Boolean) {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean("is_grid_view", isGridView)
        apply()
    }
}

private fun loadViewMode(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPref.getBoolean("is_grid_view", true)
}

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