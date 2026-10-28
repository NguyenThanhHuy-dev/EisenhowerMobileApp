package com.example.todolist.feature.tasks.ui

import androidx.annotation.StringRes
import com.example.todolist.R
import com.example.todolist.feature.tasks.reminder.ReminderOption

@get:StringRes
val ReminderOption.labelRes: Int
    get() = when (this) {
        ReminderOption.NONE -> R.string.reminder_none
        ReminderOption.IN_15_MIN -> R.string.reminder_15_min
        ReminderOption.IN_30_MIN -> R.string.reminder_30_min
        ReminderOption.IN_1_HOUR -> R.string.reminder_1_hour
        ReminderOption.IN_3_HOURS -> R.string.reminder_3_hours
        ReminderOption.TOMORROW_MORNING -> R.string.reminder_tomorrow
    }
