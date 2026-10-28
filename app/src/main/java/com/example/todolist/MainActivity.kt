package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.todolist.navigation.EisenhowerNavigation
import com.example.todolist.core.theme.ToDoListTheme
import com.example.todolist.core.audio.SoundPlayer

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SoundPlayer.loadSounds(this)
        setContent {
            ToDoListTheme() {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EisenhowerNavigation()
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.release()
    }
}
