package com.example.todolist.feature.tasks.reminder

import android.content.Context
import com.example.todolist.feature.tasks.data.Task

interface ReminderScheduler {
    fun schedule(task: Task)
    fun cancel(taskId: Long)

    object None : ReminderScheduler {
        override fun schedule(task: Task) = Unit
        override fun cancel(taskId: Long) = Unit
    }
}

class AlarmReminderScheduler(context: Context) : ReminderScheduler {
    private val appContext = context.applicationContext

    override fun schedule(task: Task) = TaskReminderManager.scheduleReminder(appContext, task)

    override fun cancel(taskId: Long) = TaskReminderManager.cancelReminder(appContext, taskId)
}
