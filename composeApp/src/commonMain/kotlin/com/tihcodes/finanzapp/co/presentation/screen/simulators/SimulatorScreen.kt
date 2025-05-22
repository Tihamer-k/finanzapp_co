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
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun SimulatorScreen(
    simulatorName: String,
    navController: NavController
) {
    var initialAmount by remember { mutableStateOf(100000.0) }
    var interestRate by remember { mutableStateOf(5.0) }
    var years by remember { mutableStateOf(1) }

    val finalAmount = initialAmount * (1 + (interestRate / 100)).pow(years)

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Simuladores",
                notificationsCount = 0,
                showBackButton = true,
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
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Simulador de interés compuesto",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Este simulador te permite calcular el crecimiento de una inversión a través del interés compuesto. Ajusta el monto inicial, la tasa de interés anual y los años para ver el total proyectado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    "Monto inicial: $${
                        formatNumberWithCommas(
                            initialAmount.roundToInt()
                        )
                    }"
                )
                Slider(
                    value = initialAmount.toFloat(),
                    onValueChange = { initialAmount = it.toDouble() },
                    valueRange = 100000.0f..9000000.0f
                )

                Text("Tasa de interés: ${interestRate.roundToInt()}%")
                Slider(
                    value = interestRate.toFloat(),
                    onValueChange = { interestRate = it.toDouble() },
                    valueRange = 1f..20f
                )

                Text("Años: $years")
                Slider(
                    value = years.toFloat(),
                    onValueChange = { years = it.toInt() },
                    valueRange = 1f..30f,
                    steps = 29
                )

                HorizontalDivider()

                Text(
                    text = "Total proyectado: $${formatNumberWithCommas(finalAmount.roundToInt())}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1B9C85)
                )
            }
        }
    }
}
