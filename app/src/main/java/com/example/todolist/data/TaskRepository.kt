// app/src/main/java/com/example/todolist/data/TaskRepository.kt
package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val quadrant1Tasks: Flow<List<Task>> = taskDao.getQuadrant1Tasks()
    val quadrant2Tasks: Flow<List<Task>> = taskDao.getQuadrant2Tasks()
    val quadrant3Tasks: Flow<List<Task>> = taskDao.getQuadrant3Tasks()
    val quadrant4Tasks: Flow<List<Task>> = taskDao.getQuadrant4Tasks()

    val completedTasks: Flow<List<Task>> = taskDao.getCompletedTasks()


    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}