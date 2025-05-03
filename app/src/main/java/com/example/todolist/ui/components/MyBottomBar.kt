package com.example.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(
    onAddTaskClick: () -> Unit,
    onFinishedTasksClick: () -> Unit
) {
    val scale = remember { mutableFloatStateOf(1f) }

    TopAppBar(
        title = {
        },
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onFinishedTasksClick() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.White), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Finished Tasks",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = {
                        scale.floatValue = 1.1f
                        onAddTaskClick()
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.White), CircleShape)// Keep some padding if needed
                        .scale(scale.floatValue)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        tint = Color.White
                    )
                }

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .height(80.dp)

    )



}

