package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.domain.model.BottomNavItem
import compose.icons.TablerIcons
import compose.icons.tablericons.Home
import compose.icons.tablericons.Medal
import compose.icons.tablericons.ReportMoney
import compose.icons.tablericons.School
import compose.icons.tablericons.TriangleSquareCircle

val items = listOf(
    BottomNavItem(
        title = "Inicio",
        route = "home",
        icon = TablerIcons.Home
    ),
    BottomNavItem(
        title = "Aprende",
        route = "learn",
        icon = TablerIcons.School
    ),
    BottomNavItem(
        title = "Registros",
        route = "records",
        icon = TablerIcons.ReportMoney
    ),
    BottomNavItem(
        title = "Categorías",
        route = "categories",
        icon = TablerIcons.TriangleSquareCircle
    ),
    BottomNavItem(
        title = "Premios",
        route = "rewards",
        icon = TablerIcons.Medal
    )
)

@Composable
fun BottomNavBar(indexIn: Int, onItemClick: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .height(140.dp),
        tonalElevation = 6.dp,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == indexIn,
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier
                                .padding(4.dp)
                                .height(if (index == indexIn) 36.dp else 30.dp)
                                .width(if (index == indexIn) 34.dp else 28.dp),
                            tint =
                                if (index == indexIn) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    },
                    label =
                        {
                            Text(
                                text = item.title,
                                color = if (index == indexIn) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = if (index == indexIn) 12.sp else 10.sp
                            )
                        },
                    onClick = {
                        onItemClick.navigate(item.route)
                    }
                )
            }
        }
    }
}