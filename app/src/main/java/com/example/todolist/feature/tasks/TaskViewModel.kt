package com.example.todolist.feature.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.feature.tasks.data.Task
import com.example.todolist.feature.tasks.data.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository,
    private val externalScope: CoroutineScope? = null
) : ViewModel() {
    private val scope: CoroutineScope
        get() = externalScope ?: viewModelScope

    val activeTasks = repository.activeTasks
    val completedTasks = repository.completedTasks

    val quadrants: StateFlow<Map<QuadrantType, List<Task>>> = activeTasks
        .map { tasks ->
            val byType = tasks.groupBy(QuadrantType::of)
            QuadrantType.entries.associateWith { byType[it].orEmpty() }
        }
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000),
            QuadrantType.entries.associateWith { emptyList() }
        )

    fun restoreTask(task: Task) {
        scope.launch {
            repository.updateTask(task.copy(isCompleted = false))
        }
    }

    fun clearCompletedTasks() {
        scope.launch {
            repository.clearCompletedTasks()
        }
    }

    fun addTask(task: Task, onInserted: ((Long) -> Unit)? = null) {
        scope.launch {
            val id = repository.addTask(task)
            onInserted?.invoke(id)
        }
    }

    fun updateTask(task: Task) {
        scope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        scope.launch {
            repository.deleteTask(task)
        }
    }

    fun getTaskById(taskId: Long): Flow<Task?> {
        return repository.getTaskById(taskId)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}