package com.example.todolist.feature.tasks.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todolist.TodoApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Alarms are dropped on reboot and on app update, so re-register them from the database. */
class ReminderRestoreReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as TodoApplication
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                app.repository.activeTasks.first().forEach(app.reminderScheduler::schedule)
            } finally {
                pending.finish()
            }
        }
    }
}
