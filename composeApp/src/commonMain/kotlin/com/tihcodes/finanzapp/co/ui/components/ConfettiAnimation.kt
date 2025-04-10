package com.tihcodes.finanzapp.co.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun ConfettiAnimation(onAnimationEnd: () -> Unit) {
    var confettiOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val duration = 1500L
        val startTime = withFrameNanos { it }
        while (true) {
            val elapsed = withFrameNanos { it } - startTime
            confettiOffset = (elapsed / 1_000_000).toFloat()
            if (elapsed > duration * 1_000_000) {
                onAnimationEnd()
                break
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
        repeat(30) {
            drawCircle(
                color = colors.random(),
                radius = 10f,
                center = Offset(
                    x = (0..size.width.toInt()).random().toFloat(),
                    y = (confettiOffset + it * 10) % size.height
                )
            )
        }
    }
}

