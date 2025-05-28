package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.presentation.viewmodel.TransactionChartViewModel
import com.tihcodes.finanzapp.co.utils.Validator.formatNumberWithCommas
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.pie.CircularLabelPositionProvider
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.pie.PieLabelPlacement
import io.github.koalaplot.core.pie.StraightLineConnector
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun TransactionPieChartWithKoalaPlot(
    viewModel: TransactionChartViewModel
) {
    val transactionType by viewModel.transactionType.collectAsState()
    val errorMessage by viewModel.errorMessageChart.collectAsState()

    val slices by viewModel.pieChartData.collectAsState()
    val total = slices.sumOf { it.value.absoluteValue }
    val labelSpacing = 1.1f

    val colors = slices.map { it.color }
    val values = slices.map { it.value.absoluteValue.toFloat() }
    val labels = slices.map { it.label }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        // Segmentos: Gastos / Ingresos
        val segments =
            listOf(TransactionType.EXPENSE to "Gastos", TransactionType.INCOME to "Ingresos")
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            segments.forEach { (type, label) ->
                val selected = type == transactionType
                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                        .clickable { viewModel.toggleType(type) }
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
            )
        }

        if (values.isNotEmpty()) {
            val dataSize = values.size
            ChartLayout(
//                title = {
//                    Text(
//                        text = if (transactionType == TransactionType.EXPENSE) "Gastos" else "Ingresos",
//                        style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                },
                legend = {
                    FlowLegend(
                        itemCount = values.size,
                        symbol = { i ->
                            Symbol(
                                modifier = Modifier.size(10.dp),
                                fillBrush = SolidColor(colors[i])
                            )
                        },
                        label = { i ->
                            Text(
                                text = labels[i],
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        modifier = Modifier.padding(top = 8.dp),
                    )
                },
                legendLocation = LegendLocation.BOTTOM
            ) {
                PieChart(
                    values = values,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    slice = { i ->
                        if (i < dataSize)
                            DefaultSlice(
                                color = colors.getOrElse(i) { Color.Gray },
                                hoverExpandFactor = 1.05f,
                                antiAlias = true,
                                gap = 1f
                            )
                    },
                    label = { i ->
                        if (i < dataSize)
                            Text(
                                "${((values[i] / total) * 100).roundToInt()}%",
                                style = MaterialTheme.typography.labelMedium
                            )
                    },
                    labelPositionProvider = CircularLabelPositionProvider(
                        labelSpacing = labelSpacing,
                        labelPlacement = PieLabelPlacement.External
                    ),
                    labelConnector = { i ->
                        if (i < dataSize)
                            StraightLineConnector(
                                connectorColor = colors.getOrElse(i) { Color.Black },
                                connectorStroke = Stroke(width = 1f)
                            )
                    },
                    holeSize = 0.6f,
                    holeContent = {
                        Column(
                            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                        ) {
                            Text(
                                "Total",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Text(
                                "$${formatNumberWithCommas(total.toInt())}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                )
            }
        } else {
            Text(
                text = "No hay datos disponibles para mostrar.",
                modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
