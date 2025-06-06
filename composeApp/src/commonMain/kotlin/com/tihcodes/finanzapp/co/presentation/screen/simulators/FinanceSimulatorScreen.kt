package com.tihcodes.finanzapp.co.presentation.screen.simulators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.presentation.components.BottomNavBar
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.utils.Validator.formatNumberWithCommas
import kotlin.math.ceil
import kotlin.math.max

@Composable
fun FinanceSimulatorScreen(
    simulatorName: String = "Simulador de Finanzas",
    navController: NavController
) {
    var goalAmount by remember { mutableStateOf(1_000_000.0) }
    var initialSavings by remember { mutableStateOf(100_000.0) }
    var monthlyContribution by remember { mutableStateOf(20_000.0) }

    val monthsRequired = if (monthlyContribution > 0 && goalAmount > initialSavings) {
        ceil((goalAmount - initialSavings) / monthlyContribution).toInt()
    } else {
        0
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Simuladores",
                showBackButton = true
            )
        },
        bottomBar = {
            BottomNavBar(
                indexIn = 4,
                onItemClick = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = simulatorName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Simulador de Meta Financiera",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Calcula cuánto tiempo necesitas para alcanzar una meta financiera según tu ahorro actual y tus aportes mensuales.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


                Text("Meta financiera: $${formatNumberWithCommas(goalAmount.toInt())}")
                Slider(
                    value = goalAmount.toFloat(),
                    onValueChange = { goalAmount = it.toDouble() },
                    valueRange = 100_000f..10_000_000f
                )

                Text("Ahorros actuales: $${formatNumberWithCommas(initialSavings.toInt())}")
                Slider(
                    value = initialSavings.toFloat(),
                    onValueChange = { initialSavings = it.toDouble() },
                    valueRange = 0f..9_000_000f
                )

                Text("Aporte mensual: $${formatNumberWithCommas(monthlyContribution.toInt())}")
                Slider(
                    value = monthlyContribution.toFloat(),
                    onValueChange = { monthlyContribution = max(it.toDouble(), 100.0) },
                    valueRange = 100f..1_000_000f
                )

                HorizontalDivider()

                Text(
                    text = if (monthsRequired <= 0) "¡Ya alcanzaste tu meta!" else
                        "Alcanzarás tu meta en $monthsRequired meses",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1B9C85)
                )
            }
        }
    }
}
