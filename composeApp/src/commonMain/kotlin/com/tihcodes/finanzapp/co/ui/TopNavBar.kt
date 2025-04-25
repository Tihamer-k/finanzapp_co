package com.tihcodes.finanzapp.co.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tihcodes.finanzapp.co.data.BottomNavItem
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_arrow_back
import finanzapp_co.composeapp.generated.resources.ic_notifications
import finanzapp_co.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource


val itemsTopBar = listOf(
    BottomNavItem(
        title = "Perfil",
        route = "profile",
        icon = Res.drawable.ic_user
    ),
    BottomNavItem(
        title = "Notificaciones",
        route = "notifications",
        icon = Res.drawable.ic_notifications
    )
)

@Composable
fun TopNavBar(
    navController: NavController,
    title: String,
    notificationsCount: Int,
    modifier: Modifier = Modifier,
    showBackButton: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val insets = WindowInsets.systemBars.asPaddingValues()

    val profileItem = itemsTopBar.firstOrNull { it.route == "profile" }
    val notificationsItem = itemsTopBar.firstOrNull { it.route == "notifications" }

    val adjustedNotificationsCount = if (currentRoute == "notifications") 0 else notificationsCount

    Surface(
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(40.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = insets.calculateTopPadding(),
                bottom = insets.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center)
            )

            if (showBackButton) {
                // Back button (left)
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            } else {
                // Profile icon (left)
                profileItem?.let { item ->
                    val isSelected = item.route == currentRoute
                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.2f else 1f,
                        animationSpec = tween(300)
                    )

                    IconButton(
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .scale(scale)
                    ) {
                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                    }
                }
            }

            // Notifications icon (right)
            notificationsItem?.let { item ->
                val isSelected = item.route == currentRoute
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
                    animationSpec = tween(durationMillis = 300)
                )
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .scale(scale)
                ) {
                    BadgedBox(
                        badge = {
                            if (adjustedNotificationsCount > 0) {
                                Badge {
                                    Text(
                                        text = if (adjustedNotificationsCount > 9) "9+" else adjustedNotificationsCount.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        ) {
                            Image(
                                painter = painterResource(item.icon),
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(iconColor)
                            )
                        }
                    }
                }
            }
        }
    }
}


