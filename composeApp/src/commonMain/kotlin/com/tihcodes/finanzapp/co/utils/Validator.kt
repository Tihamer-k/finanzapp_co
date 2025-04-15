package com.tihcodes.finanzapp.co.utils

import com.tihcodes.finanzapp.co.utils.Validator.isEmailValid
import com.tihcodes.finanzapp.co.utils.Validator.isPasswordValid

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
        // Simple check: format DD/MM/YYYY
        val regex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        return regex.matches(date)


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
}
