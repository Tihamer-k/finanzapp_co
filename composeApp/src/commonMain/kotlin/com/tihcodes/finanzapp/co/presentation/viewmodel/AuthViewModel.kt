package com.tihcodes.finanzapp.co.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.domain.model.Course
import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.model.User
import com.tihcodes.finanzapp.co.domain.repository.AuthRepository
import com.tihcodes.finanzapp.co.presentation.common.BaseViewModel
import com.tihcodes.finanzapp.co.presentation.screen.learn.getCourses
import com.tihcodes.finanzapp.co.presentation.screen.rewards.getRewardsContent
import com.tihcodes.finanzapp.co.utils.getSampleTransactions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _finalTransactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val finalTransactions: StateFlow<List<TransactionItem>> = _finalTransactions.asStateFlow()

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

    private val _authState = MutableStateFlow(false)
    val authState = _authState.asStateFlow()

    private val _isSignIn = MutableStateFlow(false)
    val isSignIn = _isSignIn.asStateFlow()

    private val _isSignedOut = MutableStateFlow(false)
    val isSignedOut = _isSignedOut.asStateFlow()

    private val _showOnboarding = MutableStateFlow(true)
    val showOnboarding = _showOnboarding.asStateFlow()


    init {
        launchWithCatchingException {
            authRepository.currentUser.collect {
                _currentUser.value = it
            }
        }
    }

    private val _courses = MutableStateFlow<List<Course>>(emptyList())

    fun loadCourses() {
        _courses.value = getCourses()
    }

    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())

    fun loadRewards(){
        _rewards.value = getRewardsContent()
    }

    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

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

    //    Sección de registros - Datos de ejemplo
    private val exampleTransactions = getSampleTransactions()

    private val _transactions = MutableStateFlow<List<TransactionItem>>(exampleTransactions)
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    // Function to check if we should show example data
    fun checkAndClearExampleData(realTransactions: List<TransactionItem>) {
        if (realTransactions.isNotEmpty() && _transactions.value == exampleTransactions) {
            // Clear example data if we have real transactions
            _transactions.value = emptyList()
            _finalTransactions.value = realTransactions
        } else if (realTransactions.isEmpty() && _transactions.value.isEmpty()) {
            // Restore example data if we have no real transactions and no example data
            _transactions.value = exampleTransactions
        }
    }


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
                TransactionType.BUDGET -> transactions.filter { it.type == TransactionType.BUDGET }
            }
        }
        .map { transactions ->
            transactions.groupBy<TransactionItem, String> { it.date.month.name }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val filteredFinalTransactions = _finalTransactions
        .combine(_filterType) { transactions, filter ->
            when (filter) {
                null -> transactions
                TransactionType.INCOME -> transactions.filter { it.type == TransactionType.INCOME }
                TransactionType.EXPENSE -> transactions.filter { it.type == TransactionType.EXPENSE }
                TransactionType.BUDGET -> transactions.filter { it.type == TransactionType.BUDGET }
            }
        }
        .map { transactions ->
            transactions.groupBy<TransactionItem, String> { it.date.month.name }
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
                authRepository.authenticate(_uiState.value.email, _uiState.value.password)
                _authState.value = true // Actualiza el estado de autenticación
                _isSignIn.value = true
                _showOnboarding.value = false
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
            authRepository.signOut()
            _isSignIn.value = false
            _isSignedOut.value = true
            _currentUser.value = null
            _authState.value = false
            _uiState.update { LoginUiState() } // Reset UI state
        }
    }

    fun onRegister() {
        launchWithCatchingException {
            _isProcessing.value = true
            try {
                authRepository.createUserFStore(
                    name = _uiState.value.name,
                    surname = _uiState.value.surname,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    password = _uiState.value.password,
                    date = _uiState.value.date
                )
                _authState.value = true // Actualiza el estado de autenticación
                _isSignIn.value = true
                _showOnboarding.value = false
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
                if (!authRepository.isExistingUser(uiState.value.email)) {
                    authRepository.resetPassword(_uiState.value.email)
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
                val userId = authRepository.currentUserId
                val userData = authRepository.getUserData(userId)
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
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun setFinalTransactions(realTransactions: List<TransactionItem>) {
        _finalTransactions.value = realTransactions
    }
}


data class LoginUiState(
    var id: String = "",
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var surname: String = "",
    var phone: String = "",
    var date: String = "",
)

