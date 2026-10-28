package com.example.todolist.feature.tasks.reminder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class ReminderOptionTest {
    private val utc = TimeZone.getTimeZone("UTC")

    private fun at(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long =
        Calendar.getInstance(utc).apply {
            clear()
            set(year, month, day, hour, minute)
        }.timeInMillis

    @Test
    fun none_hasNoTrigger() {
        assertNull(ReminderOption.NONE.triggerAt(now = 1_000L, zone = utc))
    }

    @Test
    fun relativeOptions_addTheirDelayToNow() {
        val now = at(2026, Calendar.OCTOBER, 28, 22, 0)
        assertEquals(now + 15 * 60_000L, ReminderOption.IN_15_MIN.triggerAt(now, utc))
        assertEquals(now + 30 * 60_000L, ReminderOption.IN_30_MIN.triggerAt(now, utc))
        assertEquals(now + 60 * 60_000L, ReminderOption.IN_1_HOUR.triggerAt(now, utc))
        assertEquals(now + 180 * 60_000L, ReminderOption.IN_3_HOURS.triggerAt(now, utc))
    }

    @Test
    fun tomorrowMorning_isNextDayAtNineSharp_evenAcrossMonthEnd() {
        val now = at(2026, Calendar.OCTOBER, 31, 23, 59)
        assertEquals(at(2026, Calendar.NOVEMBER, 1, 9, 0), ReminderOption.TOMORROW_MORNING.triggerAt(now, utc))
    }
}
