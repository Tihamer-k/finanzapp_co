package com.tihcodes.finanzapp.co.utils

import androidx.compose.ui.graphics.Color
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.domain.model.PieSliceData
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_entertainmentame
import finanzapp_co.composeapp.generated.resources.ic_food
import finanzapp_co.composeapp.generated.resources.ic_gifts
import finanzapp_co.composeapp.generated.resources.ic_groceries
import finanzapp_co.composeapp.generated.resources.ic_home_expenses
import finanzapp_co.composeapp.generated.resources.ic_medicine
import finanzapp_co.composeapp.generated.resources.ic_savings
import finanzapp_co.composeapp.generated.resources.ic_transport
import kotlinx.datetime.LocalDate


fun getSampleCategories(): List<CategoryItem> {
    return listOf(
        CategoryItem("Food", Res.drawable.ic_food, Color(0xFF0000FF), "Food"), // blue
        CategoryItem(
            "Transport",
            Res.drawable.ic_transport,
            Color(0xFF00FFFF),
            "Transport"
        ), // cyan
        CategoryItem("Medicine", Res.drawable.ic_medicine, Color(0xFF00FF00), "Medicine"), // green
        CategoryItem("Pantry", Res.drawable.ic_groceries, Color(0xFFFFA500), "Pantry"), // orange
        CategoryItem("Housing", Res.drawable.ic_home_expenses, Color(0xFFFF0000), "Housing"), // red
        CategoryItem("Gifts", Res.drawable.ic_gifts, Color(0xFF800080), "Gifts"), // purple
        CategoryItem("Incomes", Res.drawable.ic_savings, Color(0xFFFFD700), "Incomes"), // gold
        CategoryItem(
            "Entertainment",
            Res.drawable.ic_entertainmentame,
            Color(0xFF40E0D0), // turquoise
            "Entertainment"
        )
    )
}

fun getSampleTransactions(): List<TransactionItem> {
    return listOf(
        TransactionItem(
            "1",
            "Salarío",
            "Incomes",
            LocalDate(2024, 4, 30),
            4500000.0,
            TransactionType.INCOME,
            Res.drawable.ic_savings
        ),
        TransactionItem(
            "2",
            "mercado",
            "Pantry",
            LocalDate(2024, 4, 24),
            -890000.0,
            TransactionType.EXPENSE,
            Res.drawable.ic_groceries
        ),
        TransactionItem(
            "3",
            "Arriendo",
            "Housing",//Housing
            LocalDate(2024, 4, 8),
            -620000.0,
            TransactionType.EXPENSE,
            Res.drawable.ic_home_expenses
        ),
        TransactionItem(
            "4",
            "Transmilenio",
            "Transport",
            LocalDate(2024, 4, 8),
            -200000.0,
            TransactionType.EXPENSE,
            Res.drawable.ic_transport
        ),
        TransactionItem(
            "5",
            "Netflix",
            "Entertainment",
            LocalDate(2024, 3, 31),
            -26900.0,
            TransactionType.EXPENSE,
            Res.drawable.ic_entertainmentame
        ),
        TransactionItem(
            "6",
            "Horas extras",
            "Incomes",
            LocalDate(2024, 3, 31),
            200000.0,
            TransactionType.INCOME,
            Res.drawable.ic_savings
        )
    )
}

fun getDonutChartSampleData(): List<PieSliceData> {
    return listOf(
        PieSliceData(20000.0, "Housing", Color(0xFF6200EE)),
        PieSliceData(30000.0, "Entertainment", Color(0xFF03DAC5)),
        PieSliceData(50000.0, "Food", Color(0xFFFF5722)),
        PieSliceData(10000.0, "Transport", Color(0xFF9C27B0)),
        PieSliceData(40000.0, "Medicine", Color(0xFF2196F3)),
        PieSliceData(60000.0, "Pantry", Color(0xFFFFEB3B))
    )
}

