package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.domain.model.BottomNavItem
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_category
import finanzapp_co.composeapp.generated.resources.ic_home
import finanzapp_co.composeapp.generated.resources.ic_learn
import finanzapp_co.composeapp.generated.resources.ic_rewards
import finanzapp_co.composeapp.generated.resources.ic_transactions
import org.jetbrains.compose.resources.painterResource

val items = listOf(
    BottomNavItem(
        title = "Inicio",
        route = "home",
        icon = Res.drawable.ic_home
    ),
    BottomNavItem(
        title = "Aprende",
        route = "learn",
        icon = Res.drawable.ic_learn
    ),
    BottomNavItem(
        title = "Registros",
        route = "records",
        icon = Res.drawable.ic_transactions
    ),
    BottomNavItem(
        title = "Categorías",
        route = "categories",
        icon = Res.drawable.ic_category
    ),
    BottomNavItem(
        title = "Premios",
        route = "rewards",
        icon = Res.drawable.ic_rewards
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
        tonalElevation =  6.dp,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed {
                index, item ->
                NavigationBarItem(
                    selected = index == indexIn,
                    icon = {
                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .padding(4.dp)
                                .height(if (index == indexIn) 36.dp else 30.dp)
                                .width(if (index == indexIn) 34.dp else 28.dp),
                            colorFilter =
                                if (index == indexIn) ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                else ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                        )
                    },
                    label = if (index == indexIn) {
                        {
                        Text(
                            text = item.title,
                                color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                        }
                    } else null,
                    onClick = {
                        onItemClick.navigate(item.route)
                    }
                )
            }
        }
    }
}