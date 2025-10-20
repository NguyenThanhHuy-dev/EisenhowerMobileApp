package com.example.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.data.Task

@Composable
fun Quadrant(
    title: String,
    tasks: List<Task>,
    onTaskClick: (Long) -> Unit,
    onTaskChecked: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(width = 2.dp, color = Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp, RoundedCornerShape(16.dp))
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.1f))
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,

        )

        LazyColumn {
            items(tasks) { task -> // 'tasks' should be a List<Task>
                TaskCard(
                    task = task,
                    onClick = { onTaskClick(task.id) },
                    onCheckedChange = { checked ->
                        onTaskChecked(task, checked)
                    },
                    onDelete = { onDeleteTask(task) }
                )
            }
        }

    }
}