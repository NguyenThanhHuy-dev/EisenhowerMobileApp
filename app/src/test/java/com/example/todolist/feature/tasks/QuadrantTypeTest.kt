package com.example.todolist.feature.tasks

import com.example.todolist.feature.tasks.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class QuadrantTypeTest {
    private fun task(important: Boolean, urgent: Boolean) =
        Task(title = "t", isImportant = important, isUrgent = urgent)

    @Test
    fun of_mapsEveryImportantUrgentCombination() {
        assertEquals(QuadrantType.DO_FIRST, QuadrantType.of(task(important = true, urgent = true)))
        assertEquals(QuadrantType.SCHEDULE, QuadrantType.of(task(important = true, urgent = false)))
        assertEquals(QuadrantType.DELEGATE, QuadrantType.of(task(important = false, urgent = true)))
        assertEquals(QuadrantType.DONT_DO, QuadrantType.of(task(important = false, urgent = false)))
    }

    @Test
    fun flags_matchTheQuadrantUsedByQuickAdd() {
        QuadrantType.entries.forEach { type ->
            assertEquals(type, QuadrantType.of(task(type.isImportant, type.isUrgent)))
        }
    }
}
