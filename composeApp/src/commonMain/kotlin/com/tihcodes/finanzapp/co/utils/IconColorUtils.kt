package com.tihcodes.finanzapp.co.utils

import androidx.compose.ui.graphics.Color
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_baby
import finanzapp_co.composeapp.generated.resources.ic_entertainmentame
import finanzapp_co.composeapp.generated.resources.ic_expense
import finanzapp_co.composeapp.generated.resources.ic_food
import finanzapp_co.composeapp.generated.resources.ic_gifts
import finanzapp_co.composeapp.generated.resources.ic_groceries
import finanzapp_co.composeapp.generated.resources.ic_home_expenses
import finanzapp_co.composeapp.generated.resources.ic_income
import finanzapp_co.composeapp.generated.resources.ic_medicine
import finanzapp_co.composeapp.generated.resources.ic_moneysim
import finanzapp_co.composeapp.generated.resources.ic_savings
import finanzapp_co.composeapp.generated.resources.ic_savings_pig
import finanzapp_co.composeapp.generated.resources.ic_transport
import finanzapp_co.composeapp.generated.resources.ic_travel
import finanzapp_co.composeapp.generated.resources.ic_work
import org.jetbrains.compose.resources.DrawableResource

/**
 * Get the identifier for an icon
 * @param icon The DrawableResource to get the identifier for
 * @return The string identifier for the icon
 */
fun getIconIdentifier(icon: DrawableResource): String {
    return when (icon) {
        Res.drawable.ic_gifts -> "ic_gifts"
        Res.drawable.ic_food -> "ic_food"
        Res.drawable.ic_transport -> "ic_transport"
        Res.drawable.ic_baby -> "ic_baby"
        Res.drawable.ic_work -> "ic_work"
        Res.drawable.ic_travel -> "ic_travel"
        Res.drawable.ic_entertainmentame -> "ic_entertainmentame"
        Res.drawable.ic_moneysim -> "ic_moneysim"
        Res.drawable.ic_medicine -> "ic_medicine"
        Res.drawable.ic_home_expenses -> "ic_home_expenses"
        Res.drawable.ic_groceries -> "ic_groceries"
        Res.drawable.ic_savings_pig -> "ic_savings_pig"
        Res.drawable.ic_savings -> "ic_savings"
        Res.drawable.ic_expense -> "ic_expense"
        Res.drawable.ic_income -> "ic_income"
        else -> "ic_food" // Default icon
    }
}

/**
 * Parse icon from string representation
 * @param iconString The string representation of the icon
 * @return The DrawableResource corresponding to the icon string, or a default icon if parsing fails
 */
fun parseIconFromString(iconString: String?): DrawableResource {
    if (iconString == null) return Res.drawable.ic_food // Default icon

    return when (iconString) {
        "ic_gifts" -> Res.drawable.ic_gifts
        "ic_food" -> Res.drawable.ic_food
        "ic_transport" -> Res.drawable.ic_transport
        "ic_baby" -> Res.drawable.ic_baby
        "ic_work" -> Res.drawable.ic_work
        "ic_travel" -> Res.drawable.ic_travel
        "ic_entertainmentame" -> Res.drawable.ic_entertainmentame
        "ic_moneysim" -> Res.drawable.ic_moneysim
        "ic_medicine" -> Res.drawable.ic_medicine
        "ic_home_expenses" -> Res.drawable.ic_home_expenses
        "ic_groceries" -> Res.drawable.ic_groceries
        "ic_savings_pig" -> Res.drawable.ic_savings_pig
        "ic_savings" -> Res.drawable.ic_savings
        else -> Res.drawable.ic_food // Default icon
    }
}

/**
 * Get the identifier for a color
 * @param color The Color to get the identifier for
 * @return The string identifier for the color
 */
fun getColorIdentifier(color: Color): String {
    return when (color) {
        Color(0xFFFF0000) -> "red"
        Color(0xFF0000FF) -> "blue"
        Color(0xFF00FF00) -> "green"
        Color(0xFFFFFF00) -> "yellow"
        Color(0xFFFFA500) -> "orange"
        Color(0xFF800080) -> "purple"
        Color(0xFFFFC0CB) -> "pink"
        Color(0xFF00FFFF) -> "cyan"
        Color(0xFF808080) -> "gray"
        Color(0xFF000000) -> "black"
        Color(0xFFFFFFFF) -> "white"
        Color(0xFF8B4513) -> "brown"
        Color(0xFF40E0D0) -> "turquoise"
        Color(0xFF32CD32) -> "lime"
        Color(0xFFFFD700) -> "gold"
        Color(0xFFC0C0C0) -> "silver"
        Color(0xFFE6E6FA) -> "lavender"
        Color(0xFFFFE5B4) -> "peach"
        Color(0xFF7FFFD4) -> "aquamarine"
        Color(0xFFFF7F50) -> "coral"
        else -> "blue" // Default color
    }
}

/**
 * Parse color from string representation
 * @param colorString The string representation of the color
 * @return The Color corresponding to the color string, or a default color if parsing fails
 */
fun parseColorFromString(colorString: String?): Color {
    if (colorString == null) return Color(0xFF007AFF) // Default blue color

    return when (colorString) {
        "red" -> Color(0xFFFF0000)
        "blue" -> Color(0xFF0000FF)
        "green" -> Color(0xFF00FF00)
        "yellow" -> Color(0xFFFFFF00)
        "orange" -> Color(0xFFFFA500)
        "purple" -> Color(0xFF800080)
        "pink" -> Color(0xFFFFC0CB)
        "cyan" -> Color(0xFF00FFFF)
        "gray" -> Color(0xFF808080)
        "black" -> Color(0xFF000000)
        "white" -> Color(0xFFFFFFFF)
        "brown" -> Color(0xFF8B4513)
        "turquoise" -> Color(0xFF40E0D0)
        "lime" -> Color(0xFF32CD32)
        "gold" -> Color(0xFFFFD700)
        "silver" -> Color(0xFFC0C0C0)
        "lavender" -> Color(0xFFE6E6FA)
        "peach" -> Color(0xFFFFE5B4)
        "aquamarine" -> Color(0xFF7FFFD4)
        "coral" -> Color(0xFFFF7F50)
        else -> Color(0xFF007AFF) // Default blue color
    }
}


fun parseTransactionType(typeString: String): TransactionType {
    return when (typeString) {
        "INCOME" -> TransactionType.INCOME
        "EXPENSE" -> TransactionType.EXPENSE
        "BUDGET" -> TransactionType.BUDGET
        else -> TransactionType.EXPENSE // Default type
    }
}