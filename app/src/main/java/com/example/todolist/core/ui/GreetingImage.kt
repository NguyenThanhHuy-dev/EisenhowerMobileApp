package com.example.todolist.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun GreetingImage(
    @DrawableRes imageResId: Int,
    modifier: Modifier = Modifier,
    alpha: Float = 0.6f
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = alpha
    )
}
