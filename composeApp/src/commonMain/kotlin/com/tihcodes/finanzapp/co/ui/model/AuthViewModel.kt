package com.tihcodes.finanzapp.co.ui.model

import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.data.Course
import com.tihcodes.finanzapp.co.data.Reward
import com.tihcodes.finanzapp.co.data.RewardType
import com.tihcodes.finanzapp.co.data.TransactionItem
import com.tihcodes.finanzapp.co.data.TransactionType
import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.service.AuthService
import com.tihcodes.finanzapp.co.ui.common.BaseViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_entertainmentame
import finanzapp_co.composeapp.generated.resources.ic_groceries
import finanzapp_co.composeapp.generated.resources.ic_home_expenses
import finanzapp_co.composeapp.generated.resources.ic_savings
import finanzapp_co.composeapp.generated.resources.ic_transport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


class AuthViewModel(
    private val authService: AuthService,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled = _isButtonEnabled.asStateFlow()

    private val _authState = MutableStateFlow(false)
    val authState = _authState.asStateFlow()

    private val _isSignIn = MutableStateFlow(false)
    val isSignIn = _isSignIn.asStateFlow()

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                _isButtonEnabled.value = state.email.isNotBlank()
                        && state.password.isNotBlank()
            }
        }
    }


    init {
        launchWithCatchingException {
            authService.currentUser.collect {
                _currentUser.value = it
            }
        }
    }

    //    seccion de cursos
    private val _courses = MutableStateFlow(
        listOf(
            Course(
                "1",
                "Introducción a Finanzas",
                "Conceptos básicos",
                isUnlocked = true,
                rewardId = "medal_1"
            ),
            Course("2", "Ahorro Inteligente", "Cómo ahorrar mejor", rewardId = "medal_2"),
            Course("3", "Inversiones Básicas", "Fundamentos de inversión", rewardId = "sim_1"),
            Course("4", "Planificación Financiera", "Organiza tu futuro", rewardId = "sim_2")
        )
    )
    val courses: StateFlow<List<Course>> = _courses

    private val _rewards = MutableStateFlow<List<Reward>>(
        listOf(
            Reward("medal_1", "Medalla de Inicio", "Completaste el primer curso", RewardType.MEDAL),
            Reward("medal_2", "Medalla de Ahorro", "Dominaste el ahorro", RewardType.MEDAL),
            Reward(
                "sim_1",
                "Simulador de Inversión",
                "Prueba escenarios reales",
                RewardType.SIMULATOR
            ),
            Reward(
                "sim_2",
                "Simulador de Finanzas",
                "Organiza tu futuro financiero",
                RewardType.SIMULATOR
            )
        )
    )
    val rewards: StateFlow<List<Reward>> = _rewards

    val progress: StateFlow<Float> = _courses.map { list ->
        val total = list.size
        val completed = list.count { it.isCompleted }
        if (total == 0) 0f else completed.toFloat() / total
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)


    fun completeCourse(courseId: String) {
        val updatedCourses = _courses.value.mapIndexed { index, course ->
            when {
                course.id == courseId -> course.copy(isCompleted = true)
                course.id != courseId && index == _courses.value.indexOfFirst { it.id == courseId } + 1 ->
                    course.copy(isUnlocked = true)

                else -> course
            }
        }

        val completedCourse = _courses.value.find { it.id == courseId }
        val unlockedRewardId = completedCourse?.rewardId

        val updatedRewards = _rewards.value.map {
            if (it.id == unlockedRewardId) it.copy(isUnlocked = true)
            else it
        }

        _courses.value = updatedCourses
        _rewards.value = updatedRewards
    }

    //    Sección de registros
    private val _transactions = MutableStateFlow<List<TransactionItem>>(
        listOf(
            TransactionItem(
                "1",
                "Salarío",
                "Ingresos",
                LocalDate(2024, 4, 30),
                4500000.0,
                TransactionType.INCOME,
                Res.drawable.ic_savings
            ),
            TransactionItem(
                "2",
                "mercado",
                "Despensa",
                LocalDate(2024, 4, 24),
                -890000.0,
                TransactionType.EXPENSE,
                Res.drawable.ic_groceries
            ),
            TransactionItem(
                "3",
                "Arriendo",
                "Hogar",//Housing
                LocalDate(2024, 4, 8),
                -620000.0,
                TransactionType.EXPENSE,
                Res.drawable.ic_home_expenses
            ),
            TransactionItem(
                "4",
                "Transmilenio",
                "Transporte",
                LocalDate(2024, 4, 8),
                -200000.0,
                TransactionType.EXPENSE,
                Res.drawable.ic_transport
            ),
            TransactionItem(
                "5",
                "Netflix",
                "Entretenimiento",
                LocalDate(2024, 3, 31),
                -26900.0,
                TransactionType.EXPENSE,
                Res.drawable.ic_entertainmentame
            ),
            TransactionItem(
                "6",
                "Horas extras",
                "Ingresos",
                LocalDate(2024, 3, 31),
                200000.0,
                TransactionType.INCOME,
                Res.drawable.ic_savings
            )

        )
    )
    val transactions: StateFlow<List<TransactionItem>> = _transactions

    private val _filterType = MutableStateFlow<TransactionType?>(null)
    val filterType: StateFlow<TransactionType?> = _filterType

    fun setFilter(type: TransactionType?) {
        _filterType.value = type
    }

    val filteredTransactions = _transactions
        .combine(_filterType) { transactions, filter ->
            when (filter) {
                null -> transactions
                TransactionType.INCOME -> transactions.filter { it.type == TransactionType.INCOME }
                TransactionType.EXPENSE -> transactions.filter { it.type == TransactionType.EXPENSE }
            }
        }
        .map { transactions ->
            transactions.groupBy { it.date.month.name }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())


    // Sección de autenticación
    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
        //reset error
        if (newValue.isNotBlank()) _emailError.value = false
        // validate email format
        if (!newValue.contains("@") || !newValue.contains(".")) {
            _emailError.value = true
        }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
        //reset error
        if (newValue.isNotBlank()) _passwordError.value = false
        // validate password length
        if (newValue.length < 8) {
            _passwordError.value = true
        }
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue) }

    }

    fun onSurnameChange(newValue: String) {
        _uiState.update { it.copy(surname = newValue) }
    }

    fun onPhoneChange(newValue: String) {
        _uiState.update { it.copy(phone = newValue) }
    }

    fun onDateChange(newValue: String) {
        _uiState.update { it.copy(date = newValue) }
    }

    fun onSignInClick() {

        launchWithCatchingException {
            _isProcessing.value = true
            try {
                authService.authenticate(_uiState.value.email, _uiState.value.password)
                _authState.value = true
                _isSignIn.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
                _isSignIn.value = false
            }
            _isProcessing.value = false
        }
    }


    fun onSignOut() {
        launchWithCatchingException {
            authService.signOut()
            _isSignIn.value = false
        }
    }

    fun onRegister() {
        launchWithCatchingException {
            _isProcessing.value = true
            try {
                authService.createUserFStore(
                    name = _uiState.value.name,
                    surname = _uiState.value.surname,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    password = _uiState.value.password,
                    date = _uiState.value.date
                )
                _authState.value = true
                _isSignIn.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
                _isSignIn.value = false
            }
            _isProcessing.value = false
        }
    }

    fun onReset() {
        launchWithCatchingException {
            _isProcessing.value = true
            try {
                // Check if the email is valid before sending the reset password email
                if (!authService.isExistingUser(uiState.value.email)) {
                    authService.resetPassword(_uiState.value.email)
                    _authState.value = true
                } else {
                    _authState.value = false
                    _isProcessing.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
            }
            _isProcessing.value = false
        }
    }

    fun syncUserData() {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val userId = authService.currentUserId
                val userData = authService.getUserData(userId)
                _currentUser.value = userData
                _uiState.update {
                    it.copy(
                        name = userData.name,
                        surname = userData.surname,
                        email = userData.email,
                        phone = userData.phone,
                        date = userData.date
                    )
                }
                _authState.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
            }
            _isProcessing.value = false
        }
    }
}


data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var surname: String = "",
    var phone: String = "",
    var date: String = "",
)