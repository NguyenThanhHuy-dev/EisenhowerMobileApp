package com.example.todolist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

@Composable
fun MyBottomBar(
    onAddTaskClick: () -> Unit,
    onFinishedTasksClick: () -> Unit,
    onChangeBackgroundClick: () -> Unit // <-- Thêm action mới
) {
    // Sử dụng BottomAppBar để có ngữ nghĩa đúng hơn
    BottomAppBar(
        containerColor = Color.Transparent, // Nền trong suốt
        contentColor = Color.White,
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút xem task đã hoàn thành
                IconButton(onClick = onFinishedTasksClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Finished Tasks"
                    )
                }

                // Nút đổi ảnh nền
                IconButton(onClick = onChangeBackgroundClick) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Change Background"
                    )
                }
            }
        },
        // Nút FAB sẽ được đặt ở giữa
        floatingActionButton = {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(if (isPressed) 1.1f else 1f, label = "")

            FloatingActionButton(
                onClick = onAddTaskClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.scale(scale),
                interactionSource = interactionSource
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    )
}