package com.tihcodes.finanzapp.co.utils

object Validator {

    fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        return regex.matches(email)
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8 && password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } && password.any { it.isLowerCase() }
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
        return isNameValid(name) &&
                isNameValid(surname) &&
                isEmailValid(email) &&
                isPhoneNumberValid(phone) &&
                isDateValid(birthDate) &&
                isPasswordValid(password) && confirmPassword
    }
}
