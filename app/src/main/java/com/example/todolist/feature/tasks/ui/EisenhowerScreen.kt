package com.example.todolist.feature.tasks.ui

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.feature.tasks.QuadrantType
import com.example.todolist.feature.tasks.TaskViewModel
import com.example.todolist.feature.tasks.TaskViewModelFactory
import com.example.todolist.feature.tasks.data.Task
import com.example.todolist.feature.tasks.ui.components.MyBottomBar
import com.example.todolist.feature.tasks.ui.components.Quadrant
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EisenhowerScreen(
    onAddTask: (important: Boolean, urgent: Boolean) -> Unit,
    onViewFinishedTasks: () -> Unit,
    modifier: Modifier = Modifier,
    onTaskClick: (Long) -> Unit,
    viewModel: TaskViewModel,
) {
    val context = LocalContext.current
    val prefs = remember { (context.applicationContext as TodoApplication).userPrefs }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isGridView by remember { mutableStateOf(prefs.isGridView) }
    var backgroundUri by remember { mutableStateOf(prefs.backgroundUri) }
    var avatarUri by remember { mutableStateOf(prefs.avatarUri) }

    val pickBackground = rememberImagePicker { uri ->
        backgroundUri = uri
        prefs.backgroundUri = uri
    }
    val pickAvatar = rememberImagePicker { uri ->
        avatarUri = uri
        prefs.avatarUri = uri
    }

    val quadrants by viewModel.quadrants.collectAsStateWithLifecycle()

    val onTaskChecked: (Task, Boolean) -> Unit = { task, checked ->
        viewModel.updateTask(task.copy(isCompleted = checked))
    }
    val taskDeletedMessage = stringResource(R.string.task_deleted)
    val undoLabel = stringResource(R.string.undo)
    val onDeleteWithUndo: (Task) -> Unit = { task ->
        viewModel.deleteTask(task)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = taskDeletedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.addTask(task)
            }
        }
    }

    val panel: @Composable (QuadrantType, Modifier) -> Unit = { type, panelModifier ->
        Quadrant(
            title = stringResource(type.titleRes),
            tasks = quadrants.getValue(type),
            onTaskClick = onTaskClick,
            onTaskChecked = onTaskChecked,
            onDeleteTask = onDeleteWithUndo,
            onAddClick = { onAddTask(type.isImportant, type.isUrgent) },
            modifier = panelModifier,
            backgroundColor = type.color
        )
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
                                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                                .clickable(onClick = pickAvatar)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = getGreeting(context),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isGridView = !isGridView
                            prefs.isGridView = isGridView
                        }
                    ) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = stringResource(if (isGridView) R.string.tab_view else R.string.grid_view),
                            tint = MaterialTheme.colorScheme.onBackground
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
                onAddTaskClick = { onAddTask(false, false) },
                onFinishedTasksClick = onViewFinishedTasks,
                onChangeBackgroundClick = pickBackground
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = backgroundUri ?: R.drawable.background1,
                contentDescription = stringResource(R.string.background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isGridView) {
                    QuadrantType.entries.chunked(2).forEach { rowTypes ->
                        Row(modifier = Modifier.weight(1f)) {
                            rowTypes.forEach { type -> panel(type, Modifier.weight(1f)) }
                        }
                    }
                } else {
                    val pagerState = rememberPagerState { QuadrantType.entries.size }

                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        edgePadding = 12.dp,
                        indicator = { tabPositions ->
                            if (pagerState.currentPage < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    color = QuadrantType.entries[pagerState.currentPage].color
                                )
                            }
                        }
                    ) {
                        QuadrantType.entries.forEachIndexed { index, type ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = stringResource(type.titleRes),
                                        color = if (pagerState.currentPage == index) type.color else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
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
                        panel(QuadrantType.entries[page], Modifier)
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberImagePicker(onPicked: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            onPicked(uri.toString())
        }
    }
    return {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

fun getGreeting(context: Context): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> context.getString(R.string.good_morning)
        in 12..17 -> context.getString(R.string.good_afternoon)
        else -> context.getString(R.string.good_evening)
    }
}
