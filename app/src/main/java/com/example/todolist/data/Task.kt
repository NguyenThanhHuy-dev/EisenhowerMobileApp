// app/src/main/java/com/example/todolist/data/Task.kt
package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val isImportant: Boolean,
    val isUrgent: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
) {}

