package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.todolist.ui.EisenhowerNavigation
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.utils.SoundPlayer

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

@Composable
fun GreetingImage(
    @DrawableRes imageResId: Int,
    modifier: Modifier = Modifier,
    alpha: Float = 0.6f // Default: slightly transparent
) {
    val image: Painter = painterResource(id = imageResId)
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = alpha
    )
}

