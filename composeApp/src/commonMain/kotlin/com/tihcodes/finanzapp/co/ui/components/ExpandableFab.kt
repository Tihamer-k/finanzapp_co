package com.tihcodes.finanzapp.co.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_close
import finanzapp_co.composeapp.generated.resources.ic_expense
import finanzapp_co.composeapp.generated.resources.ic_income
import finanzapp_co.composeapp.generated.resources.ic_more
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExpandableFab(
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            AnimatedVisibility(visible = expanded) {
                SmallActionButton(
                    text = "Ingreso",
                    icon = Res.drawable.ic_income,
                    color = MaterialTheme.colorScheme.secondary, // Color para Ingreso
                    onClick = {
                        expanded = false
                        onAddIncome()
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                SmallActionButton(
                    text = "Gasto",
                    icon = Res.drawable.ic_expense,
                    color = MaterialTheme.colorScheme.error, // Color para Gasto
                    onClick = {
                        expanded = false
                        onAddExpense()
                    }
                )
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(
                        if (expanded) Res.drawable.ic_close else Res.drawable.ic_more
                    ),
                    contentDescription = if (expanded) "Cerrar" else "Agregar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SmallActionButton(
    text: String,
    icon: DrawableResource,
    color: androidx.compose.ui.graphics.Color, // Nuevo parámetro para el color
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 6.dp,
        color = color, // Usar el color especificado
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}