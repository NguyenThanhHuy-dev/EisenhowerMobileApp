package com.example.todolist.feature.tasks.reminder

import java.util.Calendar
import java.util.TimeZone

enum class ReminderOption {
    NONE, IN_15_MIN, IN_30_MIN, IN_1_HOUR, IN_3_HOURS, TOMORROW_MORNING;

    /** Epoch millis of the reminder, or null when no reminder is wanted. */
    fun triggerAt(now: Long, zone: TimeZone = TimeZone.getDefault()): Long? = when (this) {
        NONE -> null
        IN_15_MIN -> now + 15 * MINUTE
        IN_30_MIN -> now + 30 * MINUTE
        IN_1_HOUR -> now + 60 * MINUTE
        IN_3_HOURS -> now + 180 * MINUTE
        TOMORROW_MORNING -> Calendar.getInstance(zone).apply {
            timeInMillis = now
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, MORNING_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private companion object {
        const val MINUTE = 60_000L
        const val MORNING_HOUR = 9
    }
}
