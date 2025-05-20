package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.utils.Validator.formatNumberWithCommas
import com.tihcodes.finanzapp.co.utils.getDonutChartSampleData
import network.chaintech.cmpcharts.common.components.Legends
import network.chaintech.cmpcharts.common.model.LegendLabel
import network.chaintech.cmpcharts.common.model.LegendsConfig
import network.chaintech.cmpcharts.common.model.PlotType
import network.chaintech.cmpcharts.ui.piechart.charts.DonutPieChart
import network.chaintech.cmpcharts.ui.piechart.models.PieChartConfig
import network.chaintech.cmpcharts.ui.piechart.models.PieChartData


@Composable
fun getDonutChart(
    transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    userId: String
) {
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val segments = listOf("Gastos", "Ingresos")
        segments.forEachIndexed { index, label ->
            val isSelected = (selectedType == TransactionType.EXPENSE && index == 0) ||
                    (selectedType == TransactionType.INCOME && index == 1)
            androidx.compose.animation.AnimatedContent(
                targetState = isSelected,
                label = ""
            ) { selected ->
                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                        .clickable {
                            selectedType =
                                if (index == 0) TransactionType.EXPENSE else TransactionType.INCOME
                        }
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    // Obtener transacciones reales
    val transactions =
        transactionRepository.getAllTransactions(userId)
            .filter { it.type == selectedType }
            .ifEmpty { listOf() }

    val categories = categoryRepository.categories.collectAsState(initial = emptyList()).value

    val data: PieChartData = if (transactions.isNotEmpty()) {
            val slices = transactions.groupBy { it.category }.map { (categoryName, transactions) ->
                val total = transactions.sumOf { it.amount }
                val category = categories.find { it.name == categoryName }
                PieChartData.Slice(
                    value = total.toFloat()
                        .let { if (selectedType == TransactionType.EXPENSE) -it else it },
                    label = categoryName,
                    color = category?.backgroundColor ?: Color.Gray
                )
            }

            if (slices.isEmpty()) {
                getDonutChartSampleData()
            } else {
                PieChartData(slices = slices, plotType = PlotType.Donut)
            }
        } else {
            getDonutChartSampleData()
        }


    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            strokeWidth = 40f,
            labelColor = MaterialTheme.colorScheme.primary,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            labelFontWeight = FontWeight.Bold,
            isAnimationEnable = true,
            chartPadding = 60,
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

