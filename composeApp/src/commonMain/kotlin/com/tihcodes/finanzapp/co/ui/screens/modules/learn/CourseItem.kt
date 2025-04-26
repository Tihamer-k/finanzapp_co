package com.tihcodes.finanzapp.co.ui.screens.modules.learn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.data.Course

@Composable
fun CourseItem(
    course: Course,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        course.isCompleted -> Color(0xFFDFF6DD) // Verde claro si completado
        course.isUnlocked -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (course.isUnlocked || course.isCompleted) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = course.isUnlocked && !course.isCompleted, onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )

            if (course.isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✅ Completado",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF1B9C85)
                )
            } else if (!course.isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🔒 Bloqueado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}