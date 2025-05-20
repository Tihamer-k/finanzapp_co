package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.chaintech.cmpcharts.common.components.Legends
import network.chaintech.cmpcharts.common.model.LegendLabel
import network.chaintech.cmpcharts.common.model.LegendsConfig
import network.chaintech.cmpcharts.common.model.PlotType
import network.chaintech.cmpcharts.ui.piechart.charts.DonutPieChart
import network.chaintech.cmpcharts.ui.piechart.models.PieChartConfig
import network.chaintech.cmpcharts.ui.piechart.models.PieChartData


@Composable
fun getDonutChart() {
    val data = getDonutChartSampleData()
    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            strokeWidth = 60f,
            labelColor = MaterialTheme.colorScheme.primary,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            labelFontWeight = FontWeight.Bold,
            isAnimationEnable = true,
            chartPadding = 40,
            labelFontSize = 42.sp,
            backgroundColor = MaterialTheme.colorScheme.background,
        )
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Legends(
            legendsConfig = getLegendsConfigFromPieChartDataForDonutChart(
                pieChartData = data
            )
        )
        DonutPieChart(
            modifier = Modifier
                .fillMaxWidth(),
            pieChartData = data,
            pieChartConfig = pieChartConfig,
        ) { slice ->
            println("Slice seleccionado: ${slice.label} - Valor: ${slice.value}")
        }
    }
}

@Composable
fun getLegendsConfigFromPieChartDataForDonutChart(pieChartData: PieChartData): LegendsConfig {
    return LegendsConfig(
        legendLabelList = pieChartData.slices.map { slice ->
            LegendLabel(
                name = slice.label,
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

@Composable
fun getDonutChartSampleData(): PieChartData {
    return PieChartData(
        listOf(
            PieChartData.Slice(
                value = 20f,
                label = "A",
                color = Color(0xFF6200EE)
            ),
            PieChartData.Slice(
                value = 30f,
                label = "B",
                color = Color(0xFF03DAC5)
            ),
            PieChartData.Slice(
                value = 50f,
                label = "ff",
                color = Color(0xFFFF5722)
            ),
            PieChartData.Slice(
                value = 10f,
                label = "D",
                color = Color(0xFF9C27B0)
            ),
            PieChartData.Slice(
                value = 40f,
                label = "E",
                color = Color(0xFF2196F3)
            ),
            PieChartData.Slice(
                value = 60f,
                label = "F",
                color = Color(0xFFFFEB3B)
            ),
        ),
        plotType = PlotType.Donut
    )
}

