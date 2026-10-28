package com.example.todolist.feature.tasks.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
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
