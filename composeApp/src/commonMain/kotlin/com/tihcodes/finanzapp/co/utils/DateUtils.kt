package com.tihcodes.finanzapp.co.utils

import kotlinx.datetime.toLocalDate

fun formatDate(rawDate: String): String {
    return try {
        val parsed = rawDate.toLocalDate()
        "${parsed.dayOfMonth}/${parsed.monthNumber}/${parsed.year}"
    } catch (e: Exception) {
        rawDate // fallback si falla el parseo
    }
}
