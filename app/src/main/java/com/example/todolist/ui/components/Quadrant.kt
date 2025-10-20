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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolist.data.Task
import com.example.todolist.utils.SoundPlayer

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
            .background(backgroundColor.copy(alpha = 0.6f)) // Tăng alpha một chút để dễ nhìn hơn
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,

            )

        LazyColumn {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onClick = { onTaskClick(task.id) },
                    onCheckedChange = { checked ->
                        onTaskChecked(task, checked)
                        if (checked) {
                            SoundPlayer.playFinishSound() // << THÊM DÒNG NÀY

                        }
                    },
                    onDelete = {
                        onDeleteTask(task)
                        SoundPlayer.playDeleteSound()
                    }
                )
            }
        }

    }
}