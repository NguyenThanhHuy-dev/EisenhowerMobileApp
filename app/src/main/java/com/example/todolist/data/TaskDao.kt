// app/src/main/java/com/example/todolist/data/TaskDao.kt
package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isImportant = 1 AND isUrgent = 1 AND isCompleted = 0")
    fun getQuadrant1Tasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE (isImportant = 1 AND isUrgent = 0 AND isCompleted = 0)")
    fun getQuadrant2Tasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE (isImportant = 0 AND isUrgent = 1 AND isCompleted = 0)")
    fun getQuadrant3Tasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE (isImportant = 0 AND isUrgent = 0 AND isCompleted = 0)")
    fun getQuadrant4Tasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<Task?>


    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}