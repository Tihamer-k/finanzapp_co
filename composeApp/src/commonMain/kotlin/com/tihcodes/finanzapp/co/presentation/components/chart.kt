package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.presentation.viewmodel.TransactionChartViewModel
import com.tihcodes.finanzapp.co.utils.Validator.formatNumberWithCommas
import network.chaintech.cmpcharts.common.components.Legends
import network.chaintech.cmpcharts.common.model.LegendLabel
import network.chaintech.cmpcharts.common.model.LegendsConfig
import network.chaintech.cmpcharts.ui.piechart.charts.DonutPieChart
import network.chaintech.cmpcharts.ui.piechart.models.PieChartConfig
import network.chaintech.cmpcharts.ui.piechart.models.PieChartData


@Composable
fun getDonutChart(viewModel: TransactionChartViewModel) {
    val selectedType by viewModel.transactionType.collectAsState()
    val pieData by viewModel.pieChartData.collectAsState()

    val chartKey = pieData.slices.size

    val pieChartConfig = PieChartConfig(
        labelVisible = true,
        strokeWidth = 40f,
        labelColor = MaterialTheme.colorScheme.primary,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
        labelFontWeight = FontWeight.Bold,
        isAnimationEnable = true, // cambiar a false si sigue fallando
        chartPadding = 60,
        labelFontSize = 42.sp,
        backgroundColor = MaterialTheme.colorScheme.background,
    )

    Column(modifier = Modifier.fillMaxWidth()) {

        // Segmentos: Gastos / Ingresos
        val types = listOf(TransactionType.EXPENSE to "Gastos", TransactionType.INCOME to "Ingresos")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            types.forEach { (type, label) ->
                val isSelected = selectedType == type
                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                        .clickable { viewModel.toggleType(type) }
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pieData.slices.isNotEmpty()) {
            Legends(
                legendsConfig = getLegendsConfigFromPieChartDataForDonutChart(pieChartData = pieData)
            )

            key(chartKey) {
                DonutPieChart(
                    modifier = Modifier.fillMaxWidth(),
                    pieChartData = pieData,
                    pieChartConfig = pieChartConfig
                ) { slice ->
                    println("Slice seleccionado: ${slice.label} - Valor: ${slice.value}")
                }
            }

        } else {
            Text(
                text = "No hay datos suficientes para mostrar el gráfico.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun getLegendsConfigFromPieChartDataForDonutChart(pieChartData: PieChartData): LegendsConfig {
    return LegendsConfig(
        legendLabelList = pieChartData.slices.map { slice ->
            LegendLabel(
                name = "${slice.label}\n($${formatNumberWithCommas(slice.value.toDouble())})",
                brush = Brush.sweepGradient(listOf(slice.color, slice.color))
            )
        },
        textSize = 8.sp,
        gridColumnCount = 3,
        gridPaddingHorizontal = 8.dp,
        gridPaddingVertical = 4.dp,
        colorBoxSize = 18.dp,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp
        ),
        spaceBWLabelAndColorBox = 8.dp,
        legendsArrangement = Arrangement.Center,
    )
}

