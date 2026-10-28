package com.example.todolist.core.prefs

import android.content.Context
import androidx.core.content.edit

class UserPrefs(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var isGridView: Boolean
        get() = prefs.getBoolean(KEY_GRID_VIEW, true)
        set(value) = prefs.edit { putBoolean(KEY_GRID_VIEW, value) }

    var backgroundUri: String?
        get() = prefs.getString(KEY_BACKGROUND, null)
        set(value) = prefs.edit { putString(KEY_BACKGROUND, value) }

    var avatarUri: String?
        get() = prefs.getString(KEY_AVATAR, null)
        set(value) = prefs.edit { putString(KEY_AVATAR, value) }

    private companion object {
        const val KEY_GRID_VIEW = "is_grid_view"
        const val KEY_BACKGROUND = "background_uri"
        const val KEY_AVATAR = "avatar_uri"
    }
}
