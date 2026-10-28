package com.example.todolist

import android.app.Application
import com.example.todolist.core.database.AppDatabase
import com.example.todolist.feature.tasks.data.TaskRepository

class TodoApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}