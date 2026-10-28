package com.example.todolist.feature.tasks

import com.example.todolist.feature.tasks.data.Task

enum class QuadrantType(val isImportant: Boolean, val isUrgent: Boolean) {
    DO_FIRST(isImportant = true, isUrgent = true),
    SCHEDULE(isImportant = true, isUrgent = false),
    DELEGATE(isImportant = false, isUrgent = true),
    DONT_DO(isImportant = false, isUrgent = false);

    companion object {
        fun of(task: Task): QuadrantType =
            entries.first { it.isImportant == task.isImportant && it.isUrgent == task.isUrgent }
    }
}
