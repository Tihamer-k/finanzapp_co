package com.tihcodes.finanzapp.co.utils

import kotlinx.datetime.LocalDate

fun formatDate(rawDate: String): String {
    return try {
        val parsed = LocalDate.parse(rawDate)
        "${parsed.dayOfMonth}/${parsed.monthNumber}/${parsed.year}"
    } catch (e: Exception) {
        rawDate // fallback si falla el parseo
    }
}
