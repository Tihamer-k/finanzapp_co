package com.tihcodes.finanzapp.co.utils

import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

object Validator {

    fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        return regex.matches(email)
    }

    fun isPasswordValid(password: String): Pair<Boolean, String?> {
        return when {
            password.length < 8 -> Pair(false, "La contraseña debe tener al menos 8 caracteres.")
            !password.any { it.isDigit() } -> Pair(false, "La contraseña debe contener al menos un número.")
            !password.any { it.isUpperCase() } -> Pair(false, "La contraseña debe contener al menos una letra mayúscula.")
            !password.any { it.isLowerCase() } -> Pair(false, "La contraseña debe contener al menos una letra minúscula.")
            else -> Pair(true, null)
        }
    }


    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun isNameValid(name: String): Boolean {
        return name.trim().length >= 2
    }

    fun isPhoneNumberValid(phone: String): Boolean {
        val regex = Regex("^\\+?[0-9]{7,15}\$")
        return regex.matches(phone)
    }

    fun isDateValid(date: String): Boolean {
        val actualYear = LocalDate.now().year
        // Simple check: format YYYY-MM-DD
        val regex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        return regex.matches(date) && date.split("-").let {
            val year = it[0].toInt()
            val month = it[1].toInt()
            val day = it[2].toInt()
            month in 1..12 && day in 1..31 && (month != 2 || day <= 29) &&
                    (month != 4 && month != 6 && month != 9 && month != 11 || day <= 30) &&
                    year <= actualYear - 18
        }


    }

    fun isFormValid(
        name: String,
        surname: String,
        email: String,
        phone: String,
        birthDate: String,
        password: String,
        confirmPassword: Boolean,
    ): Boolean {
        val(isValidPass) = isPasswordValid(password)
        return isNameValid(name) &&
                isNameValid(surname) &&
                isEmailValid(email) &&
                isPhoneNumberValid(phone) &&
                isDateValid(birthDate) &&
                isValidPass && confirmPassword
    }

    fun formatDoubleWithCommas(value: Double): String {
        return value.toString().replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1,")
    }

}
