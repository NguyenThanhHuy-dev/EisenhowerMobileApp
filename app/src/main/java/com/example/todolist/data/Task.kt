// app/src/main/java/com/example/todolist/data/Task.kt
package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String = "",
    val isImportant: Boolean,
    val isUrgent: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
) {
    fun quadrant(): Int {
        return when {
            isImportant && isUrgent -> 1
            isImportant && !isUrgent -> 2
            !isImportant && isUrgent -> 3
            else -> 4
        }
    }
}

