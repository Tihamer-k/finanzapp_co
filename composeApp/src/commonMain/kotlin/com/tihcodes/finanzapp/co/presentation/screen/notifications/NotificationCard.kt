package com.tihcodes.finanzapp.co.presentation.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tihcodes.finanzapp.co.domain.model.NotificationItem
import com.tihcodes.finanzapp.co.utils.Validator.getIconByName
import com.tihcodes.finanzapp.co.utils.formatDate
import com.tihcodes.finanzapp.co.utils.parseColorFromString

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onClickAction: () -> Unit,
    iconAction: ImageVector,
    colorAction: Color, // Default red color
) {
    val backgroundColor = if (notification.isRead)
        MaterialTheme.colorScheme.surface
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        tonalElevation = if (notification.isRead) 1.dp else 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        parseColorFromString(notification.categoryColor)
                            .copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconByName(notification.icon),
                    contentDescription = notification.title,
                    modifier = Modifier.size(24.dp),
                    tint = parseColorFromString(notification.categoryColor)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 4,
                    lineHeight = 18.sp,
                    overflow = TextOverflow.Ellipsis
                )

                notification.categoryTag?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = parseColorFromString(notification.categoryColor),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Columna derecha con fecha arriba y botón abajo
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Fecha en la parte superior derecha
                Text(
                    text = formatDate(notification.dateTime),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                // Botón en la parte inferior derecha
                IconButton(
                    onClick = { onClickAction() },
                ) {
                    Icon(
                        imageVector = iconAction,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 4.dp),
                        tint = colorAction
                    )
                }
            }
        }
    }
}

