package com.example.todolist.feature.tasks.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todolist.R
import com.example.todolist.core.theme.AmberYellow
import com.example.todolist.core.theme.CoralPink
import com.example.todolist.core.theme.SilverGray
import com.example.todolist.core.theme.SkyBlue
import com.example.todolist.feature.tasks.QuadrantType

@get:StringRes
val QuadrantType.titleRes: Int
    get() = when (this) {
        QuadrantType.DO_FIRST -> R.string.do_first
        QuadrantType.SCHEDULE -> R.string.schedule
        QuadrantType.DELEGATE -> R.string.delegate
        QuadrantType.DONT_DO -> R.string.dont_do
    }

val QuadrantType.color: Color
    get() = when (this) {
        QuadrantType.DO_FIRST -> CoralPink
        QuadrantType.SCHEDULE -> SkyBlue
        QuadrantType.DELEGATE -> AmberYellow
        QuadrantType.DONT_DO -> SilverGray
    }

@get:StringRes
val QuadrantType.hintRes: Int
    get() = when (this) {
        QuadrantType.DO_FIRST -> R.string.hint_do_first
        QuadrantType.SCHEDULE -> R.string.hint_schedule
        QuadrantType.DELEGATE -> R.string.hint_delegate
        QuadrantType.DONT_DO -> R.string.hint_dont_do
    }

val QuadrantType.icon: ImageVector
    get() = when (this) {
        QuadrantType.DO_FIRST -> Icons.Default.LocalFireDepartment
        QuadrantType.SCHEDULE -> Icons.Default.EventAvailable
        QuadrantType.DELEGATE -> Icons.Default.Groups
        QuadrantType.DONT_DO -> Icons.Default.Block
    }

/** Stronger shade of [color] for filled buttons, so even the grey quadrant reads as enabled. */
val QuadrantType.buttonColor: Color
    get() = when (this) {
        QuadrantType.DO_FIRST -> Color(0xFFFF8A80)
        QuadrantType.SCHEDULE -> Color(0xFF4FC3F7)
        QuadrantType.DELEGATE -> Color(0xFFFFB74D)
        QuadrantType.DONT_DO -> Color(0xFF90A4AE)
    }
